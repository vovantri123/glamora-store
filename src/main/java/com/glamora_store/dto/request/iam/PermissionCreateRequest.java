package com.glamora_store.dto.request.iam;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionCreateRequest {
    @NotBlank(message = "PERMISSION_NAME_REQUIRED")
    private String name;

    private String description;
}
