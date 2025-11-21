package com.glamora_store.dto.response.common.review;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReviewResponse {

    private Long id;

    private Long userId;
    private String userFullName;
    private String userAvatar;

    private Long productId;
    private String productName;

    private Long variantId;

    private Integer rating;

    private String comment;

    private Boolean isVerifiedPurchase;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
