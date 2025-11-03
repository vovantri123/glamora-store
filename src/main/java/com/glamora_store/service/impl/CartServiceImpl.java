package com.glamora_store.service.impl;

import com.glamora_store.dto.request.user.cart.AddToCartRequest;
import com.glamora_store.dto.request.user.cart.UpdateCartItemRequest;
import com.glamora_store.dto.response.user.cart.CartResponse;
import com.glamora_store.entity.Cart;
import com.glamora_store.entity.CartItem;
import com.glamora_store.entity.ProductVariant;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.CartMapper;
import com.glamora_store.repository.CartItemRepository;
import com.glamora_store.repository.CartRepository;
import com.glamora_store.repository.ProductVariantRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.CartService;
import com.glamora_store.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

  private final CartRepository cartRepository;
  private final CartItemRepository cartItemRepository;
  private final ProductVariantRepository productVariantRepository;
  private final UserRepository userRepository;
  private final CartMapper cartMapper;

  @Override
  @Transactional
  public CartResponse getMyCart() {
    Long userId = SecurityUtil.getCurrentUserId();
    Cart cart = getOrCreateCart(userId);
    return cartMapper.toCartResponse(cart);
  }

  @Override
  @Transactional
  public CartResponse addToCart(AddToCartRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();

    // Lấy hoặc tạo giỏ hàng
    Cart cart = getOrCreateCart(userId);

    // Kiểm tra variant có tồn tại không
    ProductVariant variant = productVariantRepository.findByIdAndIsDeletedFalse(request.getVariantId())
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

    // Kiểm tra số lượng tồn kho
    if (variant.getStock() < request.getQuantity()) {
      throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
    }

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    CartItem cartItem = cartItemRepository.findByCartIdAndVariantId(cart.getId(), variant.getId())
        .orElse(null);

    if (cartItem != null) {
      // Nếu đã có thì cập nhật số lượng
      int newQuantity = cartItem.getQuantity() + request.getQuantity();
      if (variant.getStock() < newQuantity) {
        throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
      }
      cartItem.setQuantity(newQuantity);
    } else {
      // Nếu chưa có thì tạo mới
      cartItem = CartItem.builder()
          .cart(cart)
          .variant(variant)
          .quantity(request.getQuantity())
          .build();
      cart.getItems().add(cartItem);
    }

    cartItemRepository.save(cartItem);

    return cartMapper.toCartResponse(cart);
  }

  @Override
  @Transactional
  public CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    Cart cart = getOrCreateCart(userId);

    // Tìm cart item
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

    // Kiểm tra cart item có thuộc về giỏ hàng của user không
    if (!cartItem.getCart().getId().equals(cart.getId())) {
      throw new IllegalArgumentException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage());
    }

    // Kiểm tra số lượng tồn kho
    ProductVariant variant = cartItem.getVariant();
    if (variant.getStock() < request.getQuantity()) {
      throw new IllegalArgumentException(ErrorMessage.INSUFFICIENT_STOCK.getMessage());
    }

    cartItem.setQuantity(request.getQuantity());
    cartItemRepository.save(cartItem);

    Cart savedCart = cartRepository.findById(cart.getId())
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CART_NOT_FOUND.getMessage()));

    return cartMapper.toCartResponse(savedCart);
  }

  @Override
  @Transactional
  public CartResponse removeCartItem(Long cartItemId) {
    Long userId = SecurityUtil.getCurrentUserId();
    Cart cart = getOrCreateCart(userId);

    // Tìm cart item
    CartItem cartItem = cartItemRepository.findById(cartItemId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage()));

    // Kiểm tra cart item có thuộc về giỏ hàng của user không
    if (!cartItem.getCart().getId().equals(cart.getId())) {
      throw new IllegalArgumentException(ErrorMessage.CART_ITEM_NOT_FOUND.getMessage());
    }

    cart.getItems().remove(cartItem);
    cartItemRepository.delete(cartItem);

    Cart savedCart = cartRepository.findById(cart.getId())
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CART_NOT_FOUND.getMessage()));

    return cartMapper.toCartResponse(savedCart);
  }

  @Override
  @Transactional
  public void clearCart() {
    Long userId = SecurityUtil.getCurrentUserId();
    Cart cart = cartRepository.findByUserIdAndUser_IsDeletedFalse(userId)
        .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.CART_NOT_FOUND.getMessage()));

    cartItemRepository.deleteByCartId(cart.getId());
    cart.getItems().clear();
    cartRepository.save(cart);
  }

  @Override
  @Transactional
  public void removeCartItemsByVariantIds(Long userId, List<Long> variantIds) {
    Cart cart = cartRepository.findByUserIdAndUser_IsDeletedFalse(userId)
        .orElse(null);

    if (cart == null || variantIds == null || variantIds.isEmpty()) {
      return; // Nothing to remove
    }

    // Tìm và xóa các cart items có variant ID trong danh sách
    List<CartItem> itemsToRemove = cart.getItems().stream()
        .filter(item -> variantIds.contains(item.getVariant().getId()))
        .toList();

    if (!itemsToRemove.isEmpty()) {
      cart.getItems().removeAll(itemsToRemove);
      cartItemRepository.deleteAll(itemsToRemove);
      cartRepository.save(cart);
    }
  }

  private Cart getOrCreateCart(Long userId) {
    return cartRepository.findByUserIdAndUser_IsDeletedFalse(userId)
        .orElseGet(() -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_NOT_FOUND.getMessage()));

          Cart newCart = Cart.builder()
              .user(user)
              .build();
          return cartRepository.save(newCart);
        });
  }
}
