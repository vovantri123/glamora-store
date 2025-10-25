package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "order_items")
public class OrderItem extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variant_id", nullable = false)
  private ProductVariant variant;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
  private BigDecimal price; // Giá tại thời điểm đặt hàng

  @Column(nullable = false, columnDefinition = "DECIMAL(12,2)")
  private BigDecimal totalPrice; // price * quantity
}
