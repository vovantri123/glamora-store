package com.glamora_store.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRoleUpdateRequest {
  @NotEmpty(message = "List roleNames cannot be empty")
  private Set<String> roleNames;
}
