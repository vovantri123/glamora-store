package com.glamora_store.dto.request.user.payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {

  @NotNull(message = "ORDER_ID_REQUIRED")
  private Long orderId;

  @NotNull(message = "PAYMENT_METHOD_ID_REQUIRED")
  private Long paymentMethodId;
}
