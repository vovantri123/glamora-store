package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "product_reviews")
public class ProductReview extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Người viết review
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  // Sản phẩm được review
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  // Biến thể cụ thể (nếu có)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "variant_id")
  private ProductVariant variant;

  // Order đã mua (để verify purchase)
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(nullable = false)
  private Integer rating; // 1–5 sao

  @Column(columnDefinition = "TEXT")
  private String comment;
  // @ElementCollection
  // @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name =
  // "review_id"))
  // @Column(name = "image_url", columnDefinition = "TEXT")
  // @Builder.Default
  // private List<String> imageUrls = new ArrayList<>(); // Ảnh đính kèm trong
  // review (sẽ làm sau)

  @Column(name = "is_verified_purchase")
  @Builder.Default
  private Boolean isVerifiedPurchase = false; // Đã mua hàng chưa

  // Soft delete field (for moderation)
  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

}
