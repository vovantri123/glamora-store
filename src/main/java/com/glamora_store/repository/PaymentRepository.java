package com.glamora_store.repository;

import com.glamora_store.entity.Payment;
import com.glamora_store.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  List<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId);

  Optional<Payment> findFirstByOrderIdOrderByCreatedAtDesc(Long orderId);

  Optional<Payment> findByTransactionId(String transactionId);

  boolean existsByTransactionId(String transactionId);

  boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

  /**
   * Find PENDING VNPay payments (with payUrl) created before the specified time
   * Used by scheduler to check for expired payments
   */
  List<Payment> findByStatusAndCreatedAtBeforeAndPayUrlIsNotNull(
    PaymentStatus status,
    LocalDateTime createdBefore);
}
