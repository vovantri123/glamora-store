package com.glamora_store.dto.request.iam;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntrospectRequest {
    @NotBlank(message = "TOKEN_REQUIRED")
    private String token;
}
