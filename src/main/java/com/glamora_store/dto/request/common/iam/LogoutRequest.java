package com.glamora_store.dto.request.common.iam;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutRequest {
    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    private String refreshToken;
}
