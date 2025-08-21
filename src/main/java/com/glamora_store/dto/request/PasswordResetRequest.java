package com.glamora_store.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequest {
    private String email;
    private String otp;
    private String newPassword;
}
