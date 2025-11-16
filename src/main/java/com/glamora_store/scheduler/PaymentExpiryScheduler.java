package com.glamora_store.scheduler;

import com.glamora_store.entity.Payment;
import com.glamora_store.enums.PaymentStatus;
import com.glamora_store.repository.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentExpiryScheduler {

    private final PaymentRepository paymentRepository;

    @Value("${scheduler.payment.expiry-check.enabled}")
    private boolean expiryCheckEnabled;

    private static final int VNPAY_EXPIRY_MINUTES = 15;

    /**
     * Check and update expired VNPay payments
     * Runs based on configured cron expression (default: every 5 minutes)
     * Only executes if scheduler.payment.expiry-check.enabled=true
     */
    @Transactional
    @Scheduled(cron = "${scheduler.payment.expiry-check.cron}", zone = "Asia/Ho_Chi_Minh")
    public void checkExpiredPayments() {
        if (!expiryCheckEnabled) {
            // log.debug("Payment expiry check is disabled. Set
            // scheduler.payment.expiry-check.enabled=true to enable.");
            return;
        }

        try {
            LocalDateTime expiryThreshold = LocalDateTime.now().minusMinutes(VNPAY_EXPIRY_MINUTES);

            // Find PENDING VNPay payments older than 15 minutes
            List<Payment> expiredPayments = paymentRepository
                    .findByStatusAndCreatedAtBeforeAndPayUrlIsNotNull(
                            PaymentStatus.PENDING,
                            expiryThreshold);

            if (!expiredPayments.isEmpty()) {
                // Update all expired payments in batch
                expiredPayments.forEach(payment -> payment.setStatus(PaymentStatus.EXPIRED));
                paymentRepository.saveAll(expiredPayments);
                log.info("Updated {} expired VNPay payment(s) to EXPIRED status", expiredPayments.size());
            } else {
                log.debug("No expired payments found");
            }

        } catch (Exception e) {
            log.error("Failed to check expired payments", e);
        }
    }
}
