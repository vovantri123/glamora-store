package com.glamora_store.dto.response.iam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.glamora_store.enums.Gender;

import lombok.*;

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
    private Set<RoleResponse> roles;

    private LocalDateTime createdAt;
    private String createBy;
    private LocalDateTime updateAt;
    private String updateBy;
}
