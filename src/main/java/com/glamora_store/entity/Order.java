package com.glamora_store.entity;

import com.glamora_store.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "orders")
public class Order extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_code", unique = true, nullable = false)
  private String orderCode; // Mã đơn hàng để tracking

  // người đặt
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // địa chỉ nhận hàng
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id", nullable = false)
  private Address shippingAddress;

  // Voucher applied to this order (nullable - only set if user applied voucher)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "voucher_id")
  private Voucher voucher;

  @Enumerated(EnumType.STRING)
  @Column(length = 20)
  private OrderStatus status;

  @Column(name = "subtotal", columnDefinition = "DECIMAL(12,2)", nullable = false)
  private BigDecimal subtotal; // Tổng tiền hàng chưa giảm giá

  @Column(name = "discount_amount", columnDefinition = "DECIMAL(12,2)")
  @Builder.Default
  private BigDecimal discountAmount = BigDecimal.ZERO; // Số tiền giảm từ voucher

  @Column(name = "distance", columnDefinition = "DECIMAL(10,2)")
  private BigDecimal distance; // Distance from store to delivery address in km

  @Column(name = "shipping_fee", columnDefinition = "DECIMAL(10,2)")
  @Builder.Default
  private BigDecimal shippingFee = BigDecimal.ZERO;

  @Column(name = "total_amount", columnDefinition = "DECIMAL(12,2)", nullable = false)
  private BigDecimal totalAmount; // subtotal - discount + shipping

  @Column(name = "note", columnDefinition = "TEXT")
  private String note;

  @Column(name = "canceled_reason", columnDefinition = "TEXT")
  private String canceledReason; // Lý do hủy đơn (user hoặc admin) - thời gian hủy dùng updatedAt từ
                                 // AuditableEntity

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<OrderItem> orderItems = new HashSet<>();

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;
}
