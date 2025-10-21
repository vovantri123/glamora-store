package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "shipping_methods")
public class ShippingMethod extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name; // GHN, GHTK, Viettel Post, J&T Express

  @Column(nullable = false, length = 50)
  private String code; // GHN, GHTK, VTP, JNT

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "base_fee", columnDefinition = "DECIMAL(10,2)", nullable = false)
  @Builder.Default
  private BigDecimal baseFee = BigDecimal.ZERO;

  @Column(name = "fee_per_km", columnDefinition = "DECIMAL(10,2)")
  private BigDecimal feePerKm;

  @Column(name = "estimated_days")
  private Integer estimatedDays;

  @Column(name = "logo_url", columnDefinition = "TEXT")
  private String logoUrl;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true;
}
