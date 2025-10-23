package com.glamora_store.scheduler;

import com.glamora_store.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderSequenceScheduler {

    private final OrderRepository orderRepository;

    /**
     * Reset order sequence at midnight (00:00:00) every day
     * This ensures order codes restart from -0001 each day
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Ho_Chi_Minh")
    public void resetDailyOrderSequence() {
        try {
            orderRepository.resetOrderSequence();
            log.info("Successfully reset order sequence at midnight");
        } catch (Exception e) {
            log.error("Failed to reset order sequence", e);
        }
    }
}
