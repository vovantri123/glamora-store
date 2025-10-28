package com.glamora_store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    @Value("${cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins; // Dùng để xác định frontend nào được phép gọi API.

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (cookies, authorization headers, etc.)
        config.setAllowCredentials(true);

        // Allow frontend origins
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));

        // Allow all headers
        config.addAllowedHeader("*");

        // Allow all HTTP methods
        config.addAllowedMethod("*");

        // Max age for preflight requests (cache in 1 hour)
        // Preflight request is sent by browsers to check CORS policy
        // Nếu không có dòng này, trình duyệt sẽ gửi preflight request mỗi lần trước khi
        // gửi request chính.
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
