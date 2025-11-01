package com.glamora_store.service;

import java.util.List;

import com.glamora_store.dto.request.user.cart.AddToCartRequest;
import com.glamora_store.dto.request.user.cart.UpdateCartItemRequest;
import com.glamora_store.dto.response.user.cart.CartResponse;

public interface CartService {

  CartResponse getMyCart();

  CartResponse addToCart(AddToCartRequest request);

  CartResponse updateCartItem(Long cartItemId, UpdateCartItemRequest request);

  CartResponse removeCartItem(Long cartItemId);

  void clearCart();

  void removeCartItemsByVariantIds(Long userId, List<Long> variantIds);
}
