package com.glamora_store.service;

import com.glamora_store.dto.request.admin.product_preview.ProductReviewUpdateRequest;
import com.glamora_store.dto.request.user.review.CreateReviewRequest;
import com.glamora_store.dto.request.user.review.UpdateReviewRequest;
import com.glamora_store.dto.response.admin.product_review.ProductReviewAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.review.ProductRatingStatsResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import org.springframework.data.domain.Pageable;

public interface ProductReviewService {

  // User endpoints
  ProductReviewResponse createReview(CreateReviewRequest request);

  ProductReviewResponse updateMyReview(Long reviewId, UpdateReviewRequest request);

  void deleteMyReview(Long reviewId);

  ProductReviewResponse getUserReviewForProduct(Long productId, Long variantId);

  // Common endpoints
  PageResponse<ProductReviewResponse> getReviewsByProductId(Long productId, Integer rating,
      Boolean isVerifiedPurchase, Pageable pageable);

  ProductRatingStatsResponse getProductRatingStats(Long productId);

  // Admin endpoints
  ProductReviewAdminResponse updateProductReview(Long id, ProductReviewUpdateRequest request);

  void deleteReview(Long reviewId);

  ProductReviewAdminResponse activateProductReview(Long id);

  ProductReviewAdminResponse getProductReviewByIdForAdmin(Long id);

  PageResponse<ProductReviewAdminResponse> searchProductReviews(Long productId, Integer rating,
      Boolean isDeleted, Pageable pageable);

}
