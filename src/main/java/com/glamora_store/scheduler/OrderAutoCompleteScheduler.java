package com.glamora_store.scheduler;

import com.glamora_store.entity.Order;
import com.glamora_store.enums.OrderStatus;
import com.glamora_store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduler tự động chuyển đơn hàng từ SHIPPING sang COMPLETED
 * sau 7 ngày kể từ khi chuyển sang SHIPPING
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderAutoCompleteScheduler {

    private final OrderRepository orderRepository;

    /**
     * Chạy mỗi ngày lúc 2h sáng
     * Cron expression: giây phút giờ ngày tháng thứ
     */
    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void autoCompleteShippingOrders() {
        try {
            log.info("Starting auto-complete shipping orders job...");

            // Tìm tất cả đơn hàng SHIPPING đã quá 7 ngày
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            List<Order> expiredShippingOrders = orderRepository
                    .findByStatusAndUpdatedAtBefore(OrderStatus.SHIPPING, sevenDaysAgo);

            if (expiredShippingOrders.isEmpty()) {
                log.info("No shipping orders to auto-complete");
                return;
            }

            int completedCount = 0;
            for (Order order : expiredShippingOrders) {
                order.setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
                completedCount++;
                log.info("Auto-completed order: {} (Order Code: {})", order.getId(), order.getOrderCode());
            }

            log.info("Auto-complete job finished. Completed {} orders", completedCount);

        } catch (Exception e) {
            log.error("Failed to auto-complete shipping orders", e);
        }
    }
}
