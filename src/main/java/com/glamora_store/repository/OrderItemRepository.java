package com.glamora_store.repository;

import com.glamora_store.entity.OrderItem;
import com.glamora_store.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Kiểm tra xem user đã mua variant này chưa (order phải COMPLETED)
     * Dùng để verify purchase khi tạo review
     */
    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END " +
            "FROM OrderItem oi " +
            "WHERE oi.variant.id = :variantId " +
            "AND oi.order.user.id = :userId " +
            "AND oi.order.status = :status")
    boolean existsByVariantIdAndUserIdAndOrderStatus(
            @Param("variantId") Long variantId,
            @Param("userId") Long userId,
            @Param("status") OrderStatus status);
}
