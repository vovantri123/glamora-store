package com.glamora_store.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.enums.ErrorMessage;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // commence() là nơi xử lý và trả về phản hồi khi request chưa được xác thực (ví dụ token sai hoặc hết hạn),
    // giúp thay phản hồi mặc định của Spring Security bằng JSON exception tùy chỉnh.

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorMessage errorMessage = ErrorMessage.UNAUTHENTICATED;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse =
                ApiResponse.builder().message(errorMessage.getMessage()).build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
