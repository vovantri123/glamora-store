package com.glamora_store.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotEmpty;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleUpdateRequest {
    @NotEmpty(message = "List roleNames cannot be empty")
    private Set<String> roleNames;
}
