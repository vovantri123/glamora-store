package com.glamora_store.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.glamora_store.dto.request.*;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.enums.OtpPurpose;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AuthenticationService;
import com.glamora_store.service.OtpEmailService;
import com.glamora_store.service.UserService;
import com.nimbusds.jose.JOSEException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final OtpEmailService otpEmailService;
    private final UserService userService;

    //    Login
    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);

        return new ApiResponse<>(SuccessMessage.LOGIN_SUCCESS.getMessage(), response);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);

        String message = result.isValid()
                ? SuccessMessage.TOKEN_VALIDATION_SUCCESS.getMessage()
                : SuccessMessage.TOKEN_VALIDATION_FAILURE.getMessage();

        return new ApiResponse<>(message, result);
    }

    //   Register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
        userService.registerUser(request);

        return new ApiResponse<>(SuccessMessage.CREATE_USER_SUCCESS.getMessage());
    }

    @PostMapping("/verify-register-otp")
    public ApiResponse<Void> verifyRegisterOtp(@RequestParam String email, @RequestParam String otp) {
        boolean verified = otpEmailService.verifyOtp(email, otp, OtpPurpose.REGISTER);

        return new ApiResponse<>(
                verified
                        ? SuccessMessage.OTP_VERIFIED_SUCCESS.getMessage()
                        : SuccessMessage.OTP_INVALID_OR_EXPIRED.getMessage());
    }

    // Forgot Password
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        otpEmailService.sendOtp(request.getEmail(), OtpPurpose.FORGOT_PASSWORD);
        return new ApiResponse<>(SuccessMessage.OTP_SENT.getMessage());
    }

    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@RequestBody PasswordResetRequest request) {
        boolean valid = otpEmailService.verifyOtp(request.getEmail(), request.getOtp(), OtpPurpose.FORGOT_PASSWORD);
        if (!valid) {
            return new ApiResponse<>(SuccessMessage.OTP_INVALID_OR_EXPIRED.getMessage());
        }

        userService.resetPassword(request.getEmail(), request.getNewPassword());
        return new ApiResponse<>(SuccessMessage.PASSWORD_RESET_SUCCESS.getMessage());
    }
}
