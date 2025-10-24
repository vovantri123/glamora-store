package com.glamora_store.util.specification;

import com.glamora_store.entity.ProductReview;
import com.glamora_store.enums.ErrorMessage;
import org.springframework.data.jpa.domain.Specification;

public class ProductReviewSpecification {
    private ProductReviewSpecification() {
        throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    public static Specification<ProductReview> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<ProductReview> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null)
                return null;
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<ProductReview> hasProductId(Long productId) {
        return (root, query, cb) -> {
            if (productId == null)
                return null;
            return cb.equal(root.get("product").get("id"), productId);
        };
    }

    public static Specification<ProductReview> hasRating(Integer rating) {
        return (root, query, cb) -> {
            if (rating == null)
                return null;
            return cb.equal(root.get("rating"), rating);
        };
    }

    public static Specification<ProductReview> isVerifiedPurchase(Boolean isVerifiedPurchase) {
        return (root, query, cb) -> {
            if (isVerifiedPurchase == null)
                return null;
            return cb.equal(root.get("isVerifiedPurchase"), isVerifiedPurchase);
        };
    }
}
