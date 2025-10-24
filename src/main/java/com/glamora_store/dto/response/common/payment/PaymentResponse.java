package com.glamora_store.dto.response.common.payment;

import com.glamora_store.enums.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

  private Long id;
  private Long orderId;
  private String orderCode;
  private PaymentMethodResponse paymentMethod;
  private PaymentStatus status;
  private BigDecimal amount;
  private String transactionId;
  private LocalDateTime paymentDate;
  private String failedReason;
  private String payUrl; // URL mà user được redirect đến trang thanh toán của VNPay
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
