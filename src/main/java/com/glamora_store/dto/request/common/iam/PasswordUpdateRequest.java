package com.glamora_store.dto.request.common.iam;

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
public class PasswordUpdateRequest {
  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String oldPassword;

  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String newPassword;
}
