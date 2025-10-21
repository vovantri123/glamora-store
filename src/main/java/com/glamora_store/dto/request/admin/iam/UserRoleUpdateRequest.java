package com.glamora_store.dto.request.admin.iam;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleUpdateRequest {
  @NotEmpty(message = "LIST_ROLE_NAMES_EMPTY")
  private Set<String> roleNames;
}
