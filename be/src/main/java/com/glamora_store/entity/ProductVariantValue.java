package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Bảng trung gian giữa ProductVariant và AttributeValue
 * Cho phép 1 variant có nhiều attribute values
 * Ví dụ: Variant "Áo thun M Đỏ" = Size M + Color Đỏ
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_variant_values", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "variant_id", "attribute_value_id" })
})
public class ProductVariantValue extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variant_id", nullable = false)
  private ProductVariant variant;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_value_id", nullable = false)
  private AttributeValue attributeValue;
}
