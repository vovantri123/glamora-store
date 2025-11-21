package com.glamora_store.dto.response.admin.product_review;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewAdminResponse {

  private Long id;
  private Long userId;
  private String userName;
  private String userEmail;
  private Long productId;
  private String productName;
  private Long variantId;
  private String variantSku;
  private Integer rating;
  private String comment;
  private Boolean isVerifiedPurchase;
  private Boolean isDeleted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String createdBy;
  private String updatedBy;
}
