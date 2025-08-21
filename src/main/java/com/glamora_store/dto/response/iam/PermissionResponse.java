package com.glamora_store.dto.response.iam;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponse {
    private String name;
    private String description;
}
