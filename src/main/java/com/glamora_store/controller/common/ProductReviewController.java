package com.glamora_store.controller.common;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.review.ProductRatingStatsResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/reviews")
@RequiredArgsConstructor
@Tag(name = "Product Reviews - Common", description = "Common endpoints for viewing product reviews")
public class ProductReviewController {

  private final ProductReviewService reviewService;

  @GetMapping("/product/{productId}")
  @Operation(summary = "Get reviews by product", description = "Get paginated list of reviews for a specific product with optional filters for rating and verified purchase")
  public ApiResponse<PageResponse<ProductReviewResponse>> getReviewsByProductId(
    @PathVariable Long productId,
    @RequestParam(required = false) Integer rating,
    @RequestParam(required = false) Boolean isVerifiedPurchase,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "createdAt") String sortBy,
    @RequestParam(defaultValue = "desc") String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
      : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    return new ApiResponse<>(
      SuccessMessage.GET_ALL_REVIEW_SUCCESS.getMessage(),
      reviewService.getReviewsByProductId(productId, rating, isVerifiedPurchase, pageable));
  }

  @GetMapping("/product/{productId}/stats")
  @Operation(summary = "Get product rating statistics", description = "Get rating statistics for a product including average rating and count by stars")
  public ApiResponse<ProductRatingStatsResponse> getProductRatingStats(@PathVariable Long productId) {
    return new ApiResponse<>(
      SuccessMessage.GET_REVIEW_SUCCESS.getMessage(),
      reviewService.getProductRatingStats(productId));
  }
}
