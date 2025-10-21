package com.glamora_store.dto.response.admin.iam;

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
public class UserResponse {
  private Long id;
  private String fullName;
  private Gender gender;
  private LocalDate dob;
  private String email;
  private String avatar;
  private Set<RoleResponse> roles;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
