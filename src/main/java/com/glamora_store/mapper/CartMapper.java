package com.glamora_store.mapper;

import com.glamora_store.dto.response.user.cart.CartItemResponse;
import com.glamora_store.dto.response.user.cart.CartResponse;
import com.glamora_store.entity.Cart;
import com.glamora_store.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.math.BigDecimal;

@Mapper(componentModel = "spring",
  nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
  uses = {ProductMapper.class}) // để MapStruct biết sử dụng ProductMapper khi cần map nested objects.
public interface CartMapper {

  @Mapping(target = "items", source = "items")
  @Mapping(target = "totalItems", expression = "java(calculateTotalItems(cart))")
  @Mapping(target = "totalAmount", expression = "java(calculateTotalAmount(cart))")
  CartResponse toCartResponse(Cart cart);

  @Mapping(target = "variant", source = "variant")
  @Mapping(target = "subtotal", expression = "java(calculateSubtotal(cartItem))")
  CartItemResponse toCartItemResponse(CartItem cartItem);

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
