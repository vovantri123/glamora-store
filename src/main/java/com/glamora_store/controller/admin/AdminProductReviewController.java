package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.product_preview.ProductReviewUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.product_review.ProductReviewAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
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
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
@Tag(name = "Product Reviews - Admin", description = "Admin endpoints for managing all product reviews")
public class AdminProductReviewController {

  private final ProductReviewService reviewService;

  @GetMapping
  @Operation(summary = "Search all reviews", description = "Get paginated list of all reviews with filters")
  public ApiResponse<PageResponse<ProductReviewAdminResponse>> searchReviews(
      @RequestParam(required = false) Long productId,
      @RequestParam(required = false) Integer rating,
      @RequestParam(defaultValue = "false") boolean isDeleted,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    PageResponse<ProductReviewAdminResponse> result = reviewService.searchProductReviews(
        productId, rating, isDeleted, pageable);

    String message = (result.getContent().isEmpty())
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_ALL_REVIEW_SUCCESS.getMessage();

    return new ApiResponse<>(message, result);
  }

  @GetMapping("/{reviewId}")
  @Operation(summary = "Get review by ID", description = "Get detailed review information by ID")
  public ApiResponse<ProductReviewAdminResponse> getReview(@PathVariable Long reviewId) {
    return new ApiResponse<>(
        SuccessMessage.GET_REVIEW_SUCCESS.getMessage(),
        reviewService.getProductReviewByIdForAdmin(reviewId));
  }

  @PutMapping("/{reviewId}")
  @Operation(summary = "Update review", description = "Update review rating and comment")
  public ApiResponse<ProductReviewAdminResponse> updateReview(
      @PathVariable Long reviewId,
      @Valid @RequestBody ProductReviewUpdateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.UPDATE_REVIEW_SUCCESS.getMessage(),
        reviewService.updateProductReview(reviewId, request));
  }

  @DeleteMapping("/{reviewId}")
  @Operation(summary = "Delete review", description = "Delete a review (soft delete) - for moderation purposes")
  public ApiResponse<String> deleteReview(@PathVariable Long reviewId) {
    reviewService.deleteReview(reviewId);
    return new ApiResponse<>(SuccessMessage.DELETE_REVIEW_SUCCESS.getMessage());
  }

  @PutMapping("/{reviewId}/activate")
  @Operation(summary = "Activate review", description = "Restore a soft-deleted review")
  public ApiResponse<ProductReviewAdminResponse> activateReview(@PathVariable Long reviewId) {
    return new ApiResponse<>(
        "Activate product review successfully",
        reviewService.activateProductReview(reviewId));
  }
}
