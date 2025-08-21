package com.glamora_store.dto.request.iam;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateRequest {

    @NotBlank(message = "ROLE_NAME_REQUIRED")
    private String name;

    private String description;

    @NotEmpty(message = "LIST_PERMISSION_NAMES_EMPTY")
    private Set<String> permissions;
}
