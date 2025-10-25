package com.glamora_store.controller.common;

import com.glamora_store.dto.request.admin.iam.UserCreateRequest;
import com.glamora_store.dto.request.common.iam.*;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.iam.UserResponse;
import com.glamora_store.dto.response.common.iam.AuthenticationResponse;
import com.glamora_store.enums.OtpPurpose;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AuthenticationService;
import com.glamora_store.service.OtpEmailService;
import com.glamora_store.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/public/auth")
@RequiredArgsConstructor

public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final OtpEmailService otpEmailService;
  private final UserService userService;

  @PostMapping("/login")
  public ApiResponse<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = authenticationService.authenticate(request);

    return new ApiResponse<>(SuccessMessage.LOGIN_SUCCESS.getMessage(), response);
  }

  @PostMapping("/introspect")
  public ApiResponse<Void> introspect(@Valid @RequestBody IntrospectRequest request)
    throws ParseException, JOSEException {
    Boolean result = authenticationService.introspect(request);

    String message = Boolean.TRUE.equals(result)
      ? SuccessMessage.TOKEN_VALIDATION_SUCCESS.getMessage()
      : SuccessMessage.TOKEN_VALIDATION_FAILURE.getMessage();

    return new ApiResponse<>(message);
  }

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<UserResponse> register(@Valid @RequestBody UserCreateRequest request) {
    userService.registerUser(request);
    return new ApiResponse<>(SuccessMessage.CREATE_USER_SUCCESS.getMessage());
  }

  @PostMapping("/verify-register-otp")
  public ApiResponse<Void> verifyRegisterOtp(@Valid @RequestBody OtpRegisterVerifyRequest request) {
    boolean verified = otpEmailService.verifyOtp(request.getEmail(), request.getOtp(), OtpPurpose.REGISTER);

    return new ApiResponse<>(
      verified
        ? SuccessMessage.OTP_VERIFIED_SUCCESS.getMessage()
        : SuccessMessage.OTP_INVALID_OR_EXPIRED.getMessage());
  }

  @PostMapping("/forgot-password")
  public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
    otpEmailService.sendOtp(request.getEmail(), OtpPurpose.FORGOT_PASSWORD);
    return new ApiResponse<>(SuccessMessage.OTP_SENT.getMessage());
  }

  @PostMapping("/reset-password")
  public ApiResponse<Void> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
    boolean valid = otpEmailService.verifyOtp(request.getEmail(), request.getOtp(), OtpPurpose.FORGOT_PASSWORD);
    if (!valid) {
      return new ApiResponse<>(SuccessMessage.OTP_INVALID_OR_EXPIRED.getMessage());
    }
    userService.resetPassword(request.getEmail(), request.getNewPassword());
    return new ApiResponse<>(SuccessMessage.PASSWORD_RESET_SUCCESS.getMessage());
  }
}
