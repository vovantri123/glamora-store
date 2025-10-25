package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "payment_methods")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PaymentMethod extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name; // VNPay, COD,...

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "logo_url")
  private String logoUrl;

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true;
}
