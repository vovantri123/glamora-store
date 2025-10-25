package com.glamora_store.dto.request.common.iam;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequest {
  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  @NotBlank(message = "OTP_REQUIRED")
  private String otp;

  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String newPassword;
}
