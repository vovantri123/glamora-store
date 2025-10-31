package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "product_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductImage extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
  private String imageUrl;

  @Column(name = "alt_text")
  private String altText; // Mô tả ảnh cho SEO và accessibility

  @Column(name = "is_thumbnail")
  @Builder.Default
  private Boolean isThumbnail = false;

  @Column(name = "display_order")
  @Builder.Default
  private Integer displayOrder = 0;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product; // Ảnh thuộc về product nào
}
