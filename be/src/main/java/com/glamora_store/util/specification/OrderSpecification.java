package com.glamora_store.util.specification;

import com.glamora_store.entity.Order;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecification {
    private OrderSpecification() {
        throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        return (root, query, cb) -> {
            if (status == null)
                return null;
            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Order> hasUserId(Long userId) {
        return (root, query, cb) -> {
            if (userId == null)
                return null;
            return cb.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Order> hasOrderCodeLike(String orderCode) {
        return (root, query, cb) -> {
            if (orderCode == null || orderCode.trim().isEmpty())
                return null;
            return cb.like(
                    cb.lower(root.get("orderCode")), "%" + orderCode.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Order> createdBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate == null && endDate == null)
                return null;
            if (startDate != null && endDate != null)
                return cb.between(root.get("createdAt"), startDate, endDate);
            if (startDate != null)
                return cb.greaterThanOrEqualTo(root.get("createdAt"), startDate);
            return cb.lessThanOrEqualTo(root.get("createdAt"), endDate);
        };
    }

    public static Specification<Order> hasUserEmail(String email) {
        return (root, query, cb) -> {
            if (email == null || email.trim().isEmpty())
                return null;
            return cb.like(
                    cb.lower(root.get("user").get("email")), "%" + email.toLowerCase().trim() + "%");
        };
    }

    public static Specification<Order> hasUserFullName(String fullName) {
        return (root, query, cb) -> {
            if (fullName == null || fullName.trim().isEmpty())
                return null;
            return cb.like(
                    cb.lower(root.get("user").get("fullName")), "%" + fullName.toLowerCase().trim() + "%");
        };
    }
}
