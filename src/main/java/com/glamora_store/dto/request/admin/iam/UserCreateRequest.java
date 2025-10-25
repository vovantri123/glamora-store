package com.glamora_store.dto.request.admin.iam;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateRequest {

  @NotBlank(message = "FULL_NAME_REQUIRED")
  private String fullName;

  private Gender gender;

  @DobConstraint(min = 6, message = "DOB_INVALID")
  private LocalDate dob;

  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 8, max = 100, message = "PASSWORD_INVALID")
  private String password;
}
