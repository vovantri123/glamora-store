package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.review.CreateReviewRequest;
import com.glamora_store.dto.request.user.review.UpdateReviewRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/reviews")
@RequiredArgsConstructor
@Tag(name = "Product Reviews - User", description = "User endpoints for managing product reviews")
public class UserProductReviewController {

  private final ProductReviewService reviewService;

  // Endpoint để check đã review cái (product, variant) này chưa là chủ yếu
  @GetMapping("/product/{productId}")
  @Operation(summary = "Get my review for product", description = "Get current user's review for a specific product/variant")
  public ApiResponse<ProductReviewResponse> getUserReviewForProduct(
      @PathVariable Long productId,
      @RequestParam Long variantId) {
    // Mặc định @RequestParam bắt buộc (required = true).
    return new ApiResponse<>(
        SuccessMessage.GET_REVIEW_SUCCESS.getMessage(),
        reviewService.getUserReviewForProduct(productId, variantId));
  }

  @PostMapping
  @Operation(summary = "Create new review", description = "Create a new review for a product")
  public ApiResponse<ProductReviewResponse> createReview(@Valid @RequestBody CreateReviewRequest request) {
    return new ApiResponse<>(
        SuccessMessage.CREATE_REVIEW_SUCCESS.getMessage(),
        reviewService.createReview(request));
  }

  @PutMapping("/{reviewId}")
  @Operation(summary = "Update my review", description = "Update your own review")
  public ApiResponse<ProductReviewResponse> updateMyReview(
      @PathVariable Long reviewId,
      @Valid @RequestBody UpdateReviewRequest request) {
    return new ApiResponse<>(
        SuccessMessage.UPDATE_REVIEW_SUCCESS.getMessage(),
        reviewService.updateMyReview(reviewId, request));
  }

  @DeleteMapping("/{reviewId}")
  @Operation(summary = "Delete my review", description = "Delete your own review (soft delete)")
  public ApiResponse<Void> deleteMyReview(@PathVariable Long reviewId) {
    reviewService.deleteMyReview(reviewId);
    return new ApiResponse<>(SuccessMessage.DELETE_REVIEW_SUCCESS.getMessage());
  }
}
