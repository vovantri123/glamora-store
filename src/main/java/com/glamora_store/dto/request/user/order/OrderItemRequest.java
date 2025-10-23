package com.glamora_store.dto.request.user.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

  @NotNull(message = "VARIANT_ID_REQUIRED")
  private Long variantId;

  @NotNull(message = "QUANTITY_REQUIRED")
  @Min(value = 1, message = "QUANTITY_INVALID")
  private Integer quantity;
}
