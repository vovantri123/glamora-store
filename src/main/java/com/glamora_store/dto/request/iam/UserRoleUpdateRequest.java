package com.glamora_store.dto.request.iam;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleUpdateRequest {
    @NotEmpty(message = "LIST_ROLE_NAMES_EMPTY")
    private Set<String> roleNames;
}
