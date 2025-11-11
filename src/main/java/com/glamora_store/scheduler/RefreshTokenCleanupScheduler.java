package com.glamora_store.scheduler;

import com.glamora_store.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(value = "scheduler.refresh-token.cleanup.enabled", havingValue = "true", matchIfMissing = true)
public class RefreshTokenCleanupScheduler {

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Scheduled task to delete expired refresh tokens
     * Runs every day at 2:00 AM
     */
    @Scheduled(cron = "${scheduler.refresh-token.cleanup.cron:0 0 2 * * *}")
    @Transactional
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        try {
            refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
            log.info("Successfully cleaned up expired refresh tokens");
        } catch (Exception e) {
            log.error("Error cleaning up expired refresh tokens: {}", e.getMessage(), e);
        }
    }
}
