package com.glamora_store.dto.response.iam;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
  private String name;
  private String description;
  private Set<PermissionResponse> permissions;
}
