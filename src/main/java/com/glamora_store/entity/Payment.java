package com.glamora_store.entity;

import com.glamora_store.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "payments")
public class Payment extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "payment_method_id", nullable = false)
  private PaymentMethod paymentMethod;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 30, nullable = false)
  private PaymentStatus status;

  @Column(name = "amount", columnDefinition = "DECIMAL(12,2)", nullable = false)
  private BigDecimal amount; // Số tiền user thực sự thanh toán

  @Column(name = "transaction_id", unique = true)
  private String transactionId; // Mã giao dịch từ VNPay,...

  @Column(name = "payment_date")
  private LocalDateTime paymentDate;

  @Column(name = "failed_reason", columnDefinition = "TEXT")
  private String failedReason; // Lý do thất bại nếu có

  @Column(name = "pay_url", columnDefinition = "TEXT")
  private String payUrl; // URL mà user được redirect đến trang thanh toán của VNPay
  // Ví dụ: https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_TxnRef=123456...
}
