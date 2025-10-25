package com.glamora_store.repository;

import com.glamora_store.entity.UserVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long> {

  Optional<UserVoucher> findByUserIdAndVoucherIdAndIsDeletedFalse(Long userId, Long voucherId);

  List<UserVoucher> findAllByUserIdAndIsDeletedFalse(Long userId);

  int countByUserIdAndVoucherIdAndIsDeletedFalse(Long userId, Long voucherId);
}
