package com.glamora_store.dto.response.user.cart;

import com.glamora_store.dto.response.common.product.ProductVariantResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

  private Long id;
  private ProductVariantResponse variant;
  private Integer quantity;
  private BigDecimal subtotal; // quantity * price
}
