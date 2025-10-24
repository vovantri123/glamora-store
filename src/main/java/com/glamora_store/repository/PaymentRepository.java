package com.glamora_store.repository;

import com.glamora_store.entity.Payment;
import com.glamora_store.enums.PaymentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

  Optional<Payment> findByOrderId(Long orderId);

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
