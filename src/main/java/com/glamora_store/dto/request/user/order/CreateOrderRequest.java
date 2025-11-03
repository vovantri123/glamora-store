package com.glamora_store.dto.request.user.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  @NotNull(message = "ORDER_ADDRESS_ID_REQUIRED")
  private Long addressId;

  @Size(max = 500, message = "ORDER_NOTE_TOO_LONG")
  private String note;

  @NotEmpty(message = "ORDER_ITEMS_REQUIRED")
  @Valid
  private List<OrderItemRequest> items;

  // Voucher information (optional - sent from frontend if user applied voucher)
  private Long voucherId;

  private BigDecimal discountAmount; // Discount amount calculated from voucher on frontend
}
