package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "attribute_values")
public class AttributeValue extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "attribute_id")
  private Attribute attribute;

  @OneToMany(mappedBy = "attributeValue", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private Set<ProductVariantValue> variantValues = new HashSet<>();
}
