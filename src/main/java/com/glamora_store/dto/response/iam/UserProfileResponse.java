package com.glamora_store.dto.response.iam;

import com.glamora_store.enums.Gender;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {
  private Long userId;
  private String fullName;
  private Gender gender;
  private LocalDate dob;
  private String email;
  private String phoneNumber;
  private String address;
  private String image;
}
