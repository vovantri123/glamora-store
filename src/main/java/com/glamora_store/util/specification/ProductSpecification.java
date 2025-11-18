package com.glamora_store.util.specification;

import com.glamora_store.entity.Product;
import com.glamora_store.enums.ErrorMessage;

import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    private ProductSpecification() {
        throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    public static Specification<Product> isDeleted() {
        return (root, query, cb) -> cb.isTrue(root.get("isDeleted"));
    }

    public static Specification<Product> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null)
                return null;
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> hasNameLike(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty())
                return null;
            return cb.like(
                    cb.lower(root.get("name")), "%" + keyword.toLowerCase().trim() + "%");
        };
    }
}
