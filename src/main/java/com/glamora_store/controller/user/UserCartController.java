package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.cart.AddToCartRequest;
import com.glamora_store.dto.request.user.cart.UpdateCartItemRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.user.cart.CartResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class UserCartController {

  private final CartService cartService;

  @GetMapping
  public ResponseEntity<ApiResponse<CartResponse>> getMyCart() {
    CartResponse cart = cartService.getMyCart();
    return ResponseEntity.ok(
        ApiResponse.<CartResponse>builder()
            .message(SuccessMessage.GET_CART_SUCCESS.getMessage())
            .data(cart)
            .build());
  }

  @PostMapping("/items")
  public ResponseEntity<ApiResponse<CartResponse>> addToCart(@Valid @RequestBody AddToCartRequest request) {
    CartResponse cart = cartService.addToCart(request);
    return ResponseEntity.ok(
        ApiResponse.<CartResponse>builder()
            .message(SuccessMessage.ADD_TO_CART_SUCCESS.getMessage())
            .data(cart)
            .build());
  }

  @PutMapping("/items/{cartItemId}")
  public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
      @PathVariable Long cartItemId,
      @Valid @RequestBody UpdateCartItemRequest request) {
    CartResponse cart = cartService.updateCartItem(cartItemId, request);
    return ResponseEntity.ok(
        ApiResponse.<CartResponse>builder()
            .message(SuccessMessage.UPDATE_CART_ITEM_SUCCESS.getMessage())
            .data(cart)
            .build());
  }

  @DeleteMapping("/items/{cartItemId}")
  public ResponseEntity<ApiResponse<CartResponse>> removeCartItem(@PathVariable Long cartItemId) {
    CartResponse cart = cartService.removeCartItem(cartItemId);
    return ResponseEntity.ok(
        ApiResponse.<CartResponse>builder()
            .message(SuccessMessage.REMOVE_CART_ITEM_SUCCESS.getMessage())
            .data(cart)
            .build());
  }

  @DeleteMapping
  public ResponseEntity<ApiResponse<Void>> clearCart() {
    cartService.clearCart();
    return ResponseEntity.ok(
        ApiResponse.<Void>builder()
            .message(SuccessMessage.CLEAR_CART_SUCCESS.getMessage())
            .build());
  }
}
