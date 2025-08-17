package com.glamora_store.util.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.glamora_store.entity.User;

public class UserSpecification {
    private UserSpecification() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static Specification<User> isNotDeleted() {
        return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
    }

    public static Specification<User> hasFullNameLike(String fullname) {
        return (root, query, cb) -> {
            if (fullname == null || fullname.trim().isEmpty()) return null;
            return cb.like(
                    cb.lower(root.get("fullName")), "%" + fullname.toLowerCase().trim() + "%");
        };
    }

    public static Specification<User> hasDobEqual(LocalDate dob) {
        return (root, query, cb) -> {
            if (dob == null) return null;
            return cb.equal(root.get("dob"), dob);
        };
    }
}
