package com.glamora_store.dto.response;

import com.glamora_store.enums.Gender;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
  private Long userId;
  private String fullName;
  private Gender gender;
  private LocalDate dob;
  private String email;
  private String phoneNumber;
  private String address;
  private String image;

  private LocalDateTime createdAt;
  private String createBy;
  private LocalDateTime updateAt;
  private String updateBy;
}
