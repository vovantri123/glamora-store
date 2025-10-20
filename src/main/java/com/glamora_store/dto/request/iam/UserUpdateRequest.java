package com.glamora_store.dto.request.iam;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

  private String fullName;

  private Gender gender;

  @DobConstraint(min = 6, message = "DOB_INVALID")
  private LocalDate dob;

  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  private String image;
}
