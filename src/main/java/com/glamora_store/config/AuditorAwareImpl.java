package com.glamora_store.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditorAware")
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        // Optional.of(SecurityContextHolder.getContext().getAuthentication().getName())
        return Optional.of("System");
    }
}
