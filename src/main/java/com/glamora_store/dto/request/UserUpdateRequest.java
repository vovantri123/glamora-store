package com.glamora_store.dto.request;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequest {

  @NotBlank(message = "FULL_NAME_REQUIRED")
  @Size(min = 1, max = 100, message = "FULL_NAME_INVALID")
  private String fullName;

  private Gender gender;

  @DobConstraint(min = 18, message = "DOB_INVALID")
  private LocalDate dob;

  @NotBlank(message = "EMAIL_REQUIRED")
  @Email(message = "EMAIL_INVALID")
  private String email;

  @NotBlank(message = "PASSWORD_REQUIRED")
  @Size(min = 6, max = 100, message = "PASSWORD_INVALID")
  private String password;

  @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
  private String phoneNumber;

  private String address;

  private String image;
}
