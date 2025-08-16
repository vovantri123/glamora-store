package com.glamora_store.dto.response;

import com.glamora_store.entity.Role;
import com.glamora_store.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
