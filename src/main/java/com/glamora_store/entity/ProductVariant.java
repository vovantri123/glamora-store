package com.glamora_store.entity;

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
@Table(name = "product_variants")
public class ProductVariant extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String sku; // Stock Keeping Unit - Mã quản lý inventory (tự động generate)

  @Column(nullable = false, columnDefinition = "DECIMAL(10,2)")
  private BigDecimal price;

  @Column(name = "compare_at_price", columnDefinition = "DECIMAL(10,2)")
  private BigDecimal compareAtPrice; // Giá gốc để so sánh (hiển thị giá đã giảm)

  @Column(nullable = false)
  @Builder.Default
  private Integer stock = 0;

  // Soft delete field
  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @OneToMany(mappedBy = "variant", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<ProductVariantValue> variantValues = new HashSet<>();
}
