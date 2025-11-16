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
 * Scheduler to automatically complete orders that have been in SHIPPING status
 * for more than 7 days without user confirmation.
 *
 * This prevents orders from being stuck in SHIPPING status indefinitely.
 * Runs daily at 2:00 AM.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AutoCompleteOrderScheduler {

    private final OrderRepository orderRepository;

    private static final int AUTO_COMPLETE_DAYS = 7;

    /**
     * Auto-complete orders in SHIPPING status for more than 7 days
     * Runs daily at 7:00 AM
     */
    @Scheduled(cron = "0 0 7 * * *", zone = "Asia/Ho_Chi_Minh")
    @Transactional
    public void autoCompleteShippingOrders() {
        log.info("Starting auto-complete shipping orders scheduler");

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(AUTO_COMPLETE_DAYS);

        // Find all SHIPPING orders updated more than 7 days ago
        List<Order> shippingOrders = orderRepository.findByStatusAndUpdatedAtBefore(
                OrderStatus.SHIPPING, cutoffDate);

        if (shippingOrders.isEmpty()) {
            log.info("No shipping orders to auto-complete");
            return;
        }

        int completedCount = 0;
        for (Order order : shippingOrders) {
            try {
                order.setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
                completedCount++;
                log.info("Auto-completed order: {} (Order code: {})",
                        order.getId(), order.getOrderCode());
            } catch (Exception e) {
                log.error("Failed to auto-complete order: {} (Order code: {})",
                        order.getId(), order.getOrderCode(), e);
            }
        }

        log.info("Auto-complete scheduler finished. Completed {} out of {} orders",
                completedCount, shippingOrders.size());
    }
}
