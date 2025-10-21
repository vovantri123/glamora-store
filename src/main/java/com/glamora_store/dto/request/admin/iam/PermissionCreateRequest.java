package com.glamora_store.dto.request.admin.iam;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionCreateRequest {
  @NotBlank(message = "PERMISSION_NAME_REQUIRED")
  private String name;

  private String description;
}
