package com.glamora_store.dto.request.iam;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;

import lombok.*;

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

    @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    private String phoneNumber;

    private String address;

    private String image;
}
