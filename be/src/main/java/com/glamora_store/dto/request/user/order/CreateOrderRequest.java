package com.glamora_store.dto.request.user.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @NotNull(message = "ORDER_ADDRESS_ID_REQUIRED")
  private Long addressId;

  @NotNull(message = "ORDER_SHIPPING_METHOD_REQUIRED")
  private Long shippingMethodId;

  private Long voucherId;

  @Size(max = 500, message = "ORDER_NOTE_TOO_LONG")
  private String note;

  @NotEmpty(message = "ORDER_ITEMS_REQUIRED")
  @Valid
  private List<OrderItemRequest> items;
}
