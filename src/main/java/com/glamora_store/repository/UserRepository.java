package com.glamora_store.repository;

import com.glamora_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
  Optional<User> findByUserIdAndIsDeletedFalse(Long userId);

  Optional<User> findByEmailAndIsDeletedFalse(String email);
}
