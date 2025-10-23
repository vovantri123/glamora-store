package com.glamora_store.dto.request.user.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartRequest {

  @NotNull(message = "VARIANT_ID_REQUIRED")
  private Long variantId;

  @NotNull(message = "QUANTITY_REQUIRED")
  @Min(value = 1, message = "QUANTITY_INVALID")
  private Integer quantity;
}
