package com.glamora_store.dto.request;

import java.util.Set;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateRequest {
    private String name;
    private String description;
    private Set<String> permissions;
}
