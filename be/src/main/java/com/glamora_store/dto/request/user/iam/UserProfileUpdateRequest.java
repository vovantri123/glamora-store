package com.glamora_store.dto.request.user.iam;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequest {

  private String fullName;

  private Gender gender;

  @DobConstraint(min = 6, message = "DOB_INVALID")
  private LocalDate dob;

  private String avatar;
}
