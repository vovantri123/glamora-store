package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.product_preview.ProductReviewUpdateRequest;
import com.glamora_store.dto.request.user.review.CreateReviewRequest;
import com.glamora_store.dto.request.user.review.UpdateReviewRequest;
import com.glamora_store.dto.response.admin.product_review.ProductReviewAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.review.ProductRatingStatsResponse;
import com.glamora_store.dto.response.common.review.ProductReviewResponse;
import com.glamora_store.entity.*;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OrderStatus;
import com.glamora_store.mapper.ProductReviewMapper;
import com.glamora_store.repository.*;
import com.glamora_store.service.ProductReviewService;
import com.glamora_store.util.SecurityUtil;
import com.glamora_store.util.specification.ProductReviewSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductReviewServiceImpl implements ProductReviewService {

  private final ProductReviewRepository reviewRepository;
  private final ProductRepository productRepository;
  private final ProductVariantRepository productVariantRepository;
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductReviewMapper reviewMapper;

  @Override
  @Transactional
  public ProductReviewResponse createReview(CreateReviewRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    User currentUser = userRepository.findById(userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                ErrorMessage.USER_NOT_FOUND.getMessage()));

    // Validate product exists
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    // Check if user already reviewed this product
    if (reviewRepository.existsByUserIdAndProductIdAndIsDeletedFalse(userId, request.getProductId())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.REVIEW_ALREADY_EXISTS.getMessage());
    }

    // Create review entity
    ProductReview review = reviewMapper.toProductReview(request);
    review.setUser(currentUser);
    review.setProduct(product);

    // Set variant if provided
    if (request.getVariantId() != null) {
      ProductVariant variant = productVariantRepository.findById(request.getVariantId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));
      review.setVariant(variant);
    }

    // Set order and verify purchase if provided
    if (request.getOrderId() != null) {
      Order order = orderRepository.findById(request.getOrderId())
          .orElseThrow(
              () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                  ErrorMessage.ORDER_NOT_FOUND.getMessage()));

      // Check if order belongs to user and is completed
      if (!order.getUser().getId().equals(userId)) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
      }

      // Verify purchase if order is completed
      if (order.getStatus() == OrderStatus.COMPLETED) {
        review.setIsVerifiedPurchase(true);
      }

      review.setOrder(order);
    }

    ProductReview savedReview = reviewRepository.save(review);
    return reviewMapper.toProductReviewResponse(savedReview);
  }

  @Override
  @Transactional
  public ProductReviewResponse updateMyReview(Long reviewId, UpdateReviewRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();

    ProductReview review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    // Check if review belongs to current user
    if (!review.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.REVIEW_ACCESS_DENIED.getMessage());
    }

    reviewMapper.toProductReview(review, request);
    ProductReview updatedReview = reviewRepository.save(review);
    return reviewMapper.toProductReviewResponse(updatedReview);
  }

  @Override
  @Transactional
  public void deleteMyReview(Long reviewId) {
    Long userId = SecurityUtil.getCurrentUserId();

    ProductReview review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    // Check if review belongs to current user
    if (!review.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.REVIEW_ACCESS_DENIED.getMessage());
    }

    review.setIsDeleted(true);
    reviewRepository.save(review);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductReviewResponse getReviewById(Long reviewId) {
    ProductReview review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    return reviewMapper.toProductReviewResponse(review);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductReviewResponse> getMyReviews(Pageable pageable) {
    Long userId = SecurityUtil.getCurrentUserId();

    Specification<ProductReview> spec = ProductReviewSpecification.isNotDeleted()
        .and(ProductReviewSpecification.hasUserId(userId));

    Page<ProductReview> reviewPage = reviewRepository.findAll(spec, pageable);
    Page<ProductReviewResponse> responsePage = reviewPage.map(reviewMapper::toProductReviewResponse);

    return PageResponse.from(responsePage);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductReviewResponse> getReviewsByProductId(Long productId, Integer rating,
      Boolean isVerifiedPurchase, Pageable pageable) {
    // Validate product exists
    if (!productRepository.existsById(productId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    Specification<ProductReview> spec = ProductReviewSpecification.isNotDeleted()
        .and(ProductReviewSpecification.hasProductId(productId))
        .and(ProductReviewSpecification.hasRating(rating))
        .and(ProductReviewSpecification.isVerifiedPurchase(isVerifiedPurchase));

    Page<ProductReview> reviewPage = reviewRepository.findAll(spec, pageable);
    Page<ProductReviewResponse> responsePage = reviewPage.map(reviewMapper::toProductReviewResponse);

    return PageResponse.from(responsePage);
  }

  @Override
  @Transactional(readOnly = true)
  public ProductRatingStatsResponse getProductRatingStats(Long productId) {
    // Validate product exists
    if (!productRepository.existsById(productId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.PRODUCT_NOT_FOUND.getMessage());
    }

    Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
    Long totalReviews = reviewRepository.countByProductIdAndIsDeletedFalse(productId);

    Specification<ProductReview> baseSpec = ProductReviewSpecification.isNotDeleted()
        .and(ProductReviewSpecification.hasProductId(productId));

    // Count reviews by rating
    Long fiveStarCount = reviewRepository.count(baseSpec.and(ProductReviewSpecification.hasRating(5)));
    Long fourStarCount = reviewRepository.count(baseSpec.and(ProductReviewSpecification.hasRating(4)));
    Long threeStarCount = reviewRepository.count(baseSpec.and(ProductReviewSpecification.hasRating(3)));
    Long twoStarCount = reviewRepository.count(baseSpec.and(ProductReviewSpecification.hasRating(2)));
    Long oneStarCount = reviewRepository.count(baseSpec.and(ProductReviewSpecification.hasRating(1)));

    return reviewMapper.toProductRatingStatsResponse(productId, averageRating,
        totalReviews, fiveStarCount, fourStarCount, threeStarCount, twoStarCount, oneStarCount);
  }

  @Override
  @Transactional
  public void deleteReview(Long reviewId) {
    ProductReview review = reviewRepository.findByIdAndIsDeletedFalse(reviewId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    review.setIsDeleted(true);
    reviewRepository.save(review);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductReviewResponse> getAllReviews(Pageable pageable) {
    Page<ProductReview> reviewPage = reviewRepository.findAll(pageable);
    Page<ProductReviewResponse> responsePage = reviewPage.map(reviewMapper::toProductReviewResponse);

    return PageResponse.from(responsePage);
  }

  // Admin-specific methods
  @Override
  @Transactional
  public ProductReviewAdminResponse updateProductReview(Long id, ProductReviewUpdateRequest request) {
    ProductReview review = reviewRepository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    reviewMapper.updateProductReviewFromRequest(request, review);
    ProductReview updatedReview = reviewRepository.save(review);
    return reviewMapper.toProductReviewAdminResponse(updatedReview);
  }

  @Override
  @Transactional
  public ProductReviewAdminResponse activateProductReview(Long id) {
    ProductReview review = reviewRepository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.REVIEW_NOT_FOUND.getMessage()));

    review.setIsDeleted(false);
    ProductReview activatedReview = reviewRepository.save(review);
    return reviewMapper.toProductReviewAdminResponse(activatedReview);
  }

  @Override
  public ProductReviewAdminResponse getProductReviewByIdForAdmin(Long id) {
    ProductReview review = reviewRepository.findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.REVIEW_NOT_FOUND.getMessage()));
    return reviewMapper.toProductReviewAdminResponse(review);
  }

  @Override
  public PageResponse<ProductReviewAdminResponse> searchProductReviews(Long productId, Long userId, Integer rating,
      Boolean isDeleted, Pageable pageable) {
    Specification<ProductReview> spec = Specification.allOf();

    if (isDeleted != null) {
      if (isDeleted) {
        spec = spec.and(ProductReviewSpecification.isDeleted());
      } else {
        spec = spec.and(ProductReviewSpecification.isNotDeleted());
      }
    }

    spec = spec.and(ProductReviewSpecification.hasProductId(productId))
        .and(ProductReviewSpecification.hasUserId(userId))
        .and(ProductReviewSpecification.hasRating(rating));

    Page<ProductReview> reviews = reviewRepository.findAll(spec, pageable);
    return PageResponse.from(reviews.map(reviewMapper::toProductReviewAdminResponse));
  }
}
