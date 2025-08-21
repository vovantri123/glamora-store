package com.glamora_store.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.glamora_store.enums.Gender;
import com.glamora_store.validator.DobConstraint;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateRequest {

    @NotBlank(message = "FULL_NAME_REQUIRED")
    @Size(min = 1, max = 100, message = "FULL_NAME_INVALID")
    private String fullName;

    private Gender gender;

    @DobConstraint(min = 18, message = "DOB_INVALID")
    private LocalDate dob;

    @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
    private String phoneNumber;

    private String address;

    private String image;
}
