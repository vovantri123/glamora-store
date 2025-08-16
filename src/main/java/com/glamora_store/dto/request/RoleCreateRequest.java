package com.glamora_store.dto.request;


import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleCreateRequest {
  private String name;
  private String description;
  private Set<String> permissions;
}
