package com.glamora_store.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.glamora_store.enums.ErrorMessage;

public class SecurityUtil {

    private SecurityUtil() {
        throw new UnsupportedOperationException(ErrorMessage.UTILITY_CLASS_SHOULD_NOT_BE_INSTANTIATED.getMessage());
    }

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            Object userId = jwt.getClaims().get("userId");
            if (userId instanceof Number number) {
                return number.longValue();
            }
        }
        throw new IllegalStateException("Unable to extract userId from JWT token");
    }
}