package com.glamora_store.mapper;

import com.glamora_store.dto.request.user.review.CreateReviewRequest;
import com.glamora_store.dto.request.user.review.UpdateReviewRequest;
import com.glamora_store.dto.response.common.review.ProductRatingStatsResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductReviewMapper {
  ProductReview toProductReview(CreateReviewRequest request);

  void toProductReview(@MappingTarget ProductReview review, UpdateReviewRequest request);

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "userFullName", source = "user.fullName")
  @Mapping(target = "userAvatar", source = "user.avatar")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "variantId", source = "variant.id")
  @Mapping(target = "orderId", source = "order.id")
  ProductReviewResponse toProductReviewResponse(ProductReview review);

  @Mapping(target = "averageRating", expression = "java(averageRating != null ? averageRating : 0.0)")
  ProductRatingStatsResponse toProductRatingStatsResponse(Long productId, Double averageRating, Long totalReviews,
                                                          Long fiveStarCount, Long fourStarCount, Long threeStarCount,
                                                          Long twoStarCount, Long oneStarCount);
}
