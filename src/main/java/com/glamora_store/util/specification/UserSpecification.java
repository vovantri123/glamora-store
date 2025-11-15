package com.glamora_store.util.specification;

import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class UserSpecification {
  private UserSpecification() {
    throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
  }

  public static Specification<User> isNotDeleted() {
    return (root, query, cb) -> cb.isFalse(root.get("isDeleted"));
  }

  public static Specification<User> isDeleted() {
    return (root, query, cb) -> cb.isTrue(root.get("isDeleted"));
  }

  public static Specification<User> hasFullNameLike(String fullname) {
    return (root, query, cb) -> {
      if (fullname == null || fullname.trim().isEmpty())
        return null;
      return cb.like(
          cb.lower(root.get("fullName")), "%" + fullname.toLowerCase().trim() + "%");
    };
  }

  public static Specification<User> hasDobEqual(LocalDate dob) {
    return (root, query, cb) -> {
      if (dob == null)
        return null;
      return cb.equal(root.get("dob"), dob);
    };
  }
}
