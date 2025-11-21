package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.product_preview.ProductReviewUpdateRequest;
import com.glamora_store.dto.request.user.review.CreateReviewRequest;
import com.glamora_store.dto.request.user.review.UpdateReviewRequest;
import com.glamora_store.dto.response.admin.product_review.ProductReviewAdminResponse;
import com.glamora_store.dto.response.common.review.ProductRatingStatsResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.entity.ProductReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProductReviewMapper {
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "variant", ignore = true)
  @Mapping(target = "isVerifiedPurchase", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  ProductReview toProductReview(CreateReviewRequest request);

  void toProductReview(@MappingTarget ProductReview review, UpdateReviewRequest request);

  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "userFullName", source = "user.fullName")
  @Mapping(target = "userAvatar", source = "user.avatar")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "variantId", source = "variant.id")
  ProductReviewResponse toProductReviewResponse(ProductReview review);

  @Mapping(target = "averageRating", expression = "java(averageRating != null ? averageRating : 0.0)")
  ProductRatingStatsResponse toProductRatingStatsResponse(Long productId, Double averageRating, Long totalReviews,
      Long fiveStarCount, Long fourStarCount, Long threeStarCount,
      Long twoStarCount, Long oneStarCount);

  // Admin mappings
  @Mapping(target = "userId", source = "user.id")
  @Mapping(target = "userName", source = "user.fullName")
  @Mapping(target = "userEmail", source = "user.email")
  @Mapping(target = "productId", source = "product.id")
  @Mapping(target = "productName", source = "product.name")
  @Mapping(target = "variantId", source = "variant.id")
  @Mapping(target = "variantSku", source = "variant.sku")
  ProductReviewAdminResponse toProductReviewAdminResponse(ProductReview productReview);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "user", ignore = true)
  @Mapping(target = "product", ignore = true)
  @Mapping(target = "variant", ignore = true)
  @Mapping(target = "isVerifiedPurchase", ignore = true)
  @Mapping(target = "isDeleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  void updateProductReviewFromRequest(ProductReviewUpdateRequest request, @MappingTarget ProductReview productReview);
}
