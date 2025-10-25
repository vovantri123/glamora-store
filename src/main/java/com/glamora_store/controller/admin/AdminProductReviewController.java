package com.glamora_store.controller.admin;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
@Tag(name = "Product Reviews - Admin", description = "Admin endpoints for managing all product reviews")
public class AdminProductReviewController {

    private final ProductReviewService reviewService;

    @GetMapping
    @Operation(summary = "Get all reviews", description = "Get paginated list of all reviews in the system")
    public ApiResponse<PageResponse<ProductReviewResponse>> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return new ApiResponse<>(
                SuccessMessage.GET_ALL_REVIEW_SUCCESS.getMessage(),
                reviewService.getAllReviews(pageable));
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete review", description = "Delete a review (soft delete) - for moderation purposes")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return new ApiResponse<>(SuccessMessage.DELETE_REVIEW_SUCCESS.getMessage());
    }
}
