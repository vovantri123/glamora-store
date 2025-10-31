package com.glamora_store.mapper;

import com.glamora_store.dto.response.user.cart.CartItemResponse;
import com.glamora_store.dto.response.user.cart.CartResponse;
import com.glamora_store.entity.Cart;
import com.glamora_store.entity.CartItem;
import com.glamora_store.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CartMapper {

  @Mapping(target = "items", source = "items")
  @Mapping(target = "totalItems", expression = "java(calculateTotalItems(cart))")
  @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(cart))")
  CartResponse toCartResponse(Cart cart);

  @Mapping(target = "variant", expression = "java(toCartProductVariantResponse(cartItem))")
  @Mapping(target = "subtotal", expression = "java(calculateSubtotal(cartItem))")
  CartItemResponse toCartItemResponse(CartItem cartItem);

  default CartItemResponse.CartProductVariantResponse toCartProductVariantResponse(CartItem cartItem) {
    if (cartItem == null || cartItem.getVariant() == null) {
      return null;
    }
    var variant = cartItem.getVariant();
    var product = variant.getProduct();

    // Get thumbnail or first image
    String imageUrl = product.getImages().stream()
        .filter(ProductImage::getIsThumbnail)
        .findFirst()
        .map(ProductImage::getImageUrl)
        .or(() -> product.getImages().stream()
            .findFirst()
            .map(ProductImage::getImageUrl))
        .orElse(null);

    return CartItemResponse.CartProductVariantResponse.builder()
        .id(variant.getId())
        .sku(variant.getSku())
        .price(variant.getPrice())
        .compareAtPrice(variant.getCompareAtPrice())
        .stock(variant.getStock())
        .imageUrl(imageUrl)
        .product(CartItemResponse.ProductInfo.builder()
            .id(product.getId())
            .name(product.getName())
            .build())
        .attributes(variant.getVariantValues().stream()
            .map(vv -> CartItemResponse.VariantAttributeResponse.builder()
                .attributeId(vv.getAttributeValue().getAttribute().getId())
                .attributeName(vv.getAttributeValue().getAttribute().getName())
                .valueId(vv.getAttributeValue().getId())
                .valueName(vv.getAttributeValue().getValue())
                .build())
            .collect(Collectors.toList()))
        .build();
  }

  default Integer calculateTotalItems(Cart cart) {
    if (cart == null || cart.getItems() == null) {
      return 0;
    }
    return cart.getItems().stream()
        .mapToInt(CartItem::getQuantity)
        .sum();
  }

  default BigDecimal calculateTotalAmount(Cart cart) {
    if (cart == null || cart.getItems() == null) {
      return BigDecimal.ZERO;
    }
    return cart.getItems().stream()
        .map(this::calculateSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  default BigDecimal calculateSubtotal(CartItem cartItem) {
    if (cartItem == null || cartItem.getVariant() == null) {
      return BigDecimal.ZERO;
    }
    BigDecimal price = cartItem.getVariant().getPrice();
    if (price == null) {
      price = BigDecimal.ZERO;
    }
    return price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
  }
}
