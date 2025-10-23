package com.glamora_store.dto.request.admin.order;

import com.glamora_store.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderStatusRequest {

  @NotNull(message = "ORDER_STATUS_REQUIRED")
  private OrderStatus status;

  private String note;
}
