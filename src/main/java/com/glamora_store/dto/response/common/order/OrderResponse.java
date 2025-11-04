package com.glamora_store.dto.response.common.order;

import com.glamora_store.enums.OrderStatus;
import com.glamora_store.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
  private Long id;
  private String orderCode;
  private OrderStatus status;
  private PaymentStatus paymentStatus;

  // Payment method info (saved at order creation)
  private Long paymentMethodId;
  private String paymentMethodName; // COD, VNPay, etc.

  private BigDecimal subtotal;
  private BigDecimal discountAmount;
  private BigDecimal distance; // Distance from store to delivery address in km
  private BigDecimal shippingFee;
  private BigDecimal totalAmount;
  private String note;
  private String canceledReason;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  // User info
  private Long userId;
  private String userEmail;
  private String userFullName;

  // Shipping address
  private Long addressId;
  private String shippingAddressDetail;

  // Recipient information (snapshot at order creation time)
  private String recipientName;
  private String recipientPhone;

  // Order items
  private List<OrderItemResponse> orderItems;
}
