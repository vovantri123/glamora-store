package com.glamora_store.dto.response.common.review;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRatingStatsResponse {

    private Long productId;

    private Double averageRating;

    private Long totalReviews;

    private Long fiveStarCount;

    private Long fourStarCount;

    private Long threeStarCount;

    private Long twoStarCount;

    private Long oneStarCount;
}
