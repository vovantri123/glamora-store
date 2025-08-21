package com.glamora_store.dto.response;

import java.time.LocalDate;

import com.glamora_store.enums.Gender;

import lombok.*;

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
