package com.glamora_store.repository;

import com.glamora_store.entity.Order;
import com.glamora_store.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
  Optional<Order> findByOrderCode(String orderCode);

  @Query("SELECT o FROM Order o WHERE " +
      "(:status IS NULL OR o.status = :status) AND " +
      "(:userId IS NULL OR o.user.id = :userId) AND " +
      "(:orderCode IS NULL OR LOWER(o.orderCode) LIKE LOWER(CONCAT('%', :orderCode, '%')))")
  Page<Order> searchOrders(
      @Param("status") OrderStatus status,
      @Param("userId") Long userId,
      @Param("orderCode") String orderCode,
      Pageable pageable);

  @Query(value = "SELECT nextval('order_daily_sequence')", nativeQuery = true)
  Long getNextOrderSequence();

  @Query(value = "SELECT setval('order_daily_sequence', 1, false)", nativeQuery = true)
  void resetOrderSequence();

  // Find orders by status and updated before specific date (for auto-complete
  // scheduler)
  List<Order> findByStatusAndUpdatedAtBefore(OrderStatus status, LocalDateTime updatedAt);
}
