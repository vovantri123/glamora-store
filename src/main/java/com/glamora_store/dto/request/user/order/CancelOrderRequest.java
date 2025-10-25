package com.glamora_store.dto.request.user.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelOrderRequest {

  @NotBlank(message = "ORDER_CANCEL_REASON_REQUIRED")
  @Size(min = 10, max = 500, message = "ORDER_CANCEL_REASON_INVALID")
  private String canceledReason;
}
