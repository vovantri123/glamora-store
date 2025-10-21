package com.glamora_store.controller.user;

import com.glamora_store.dto.request.common.iam.PasswordUpdateRequest;
import com.glamora_store.dto.request.user.iam.UserProfileUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.iam.UserResponse;
import com.glamora_store.dto.response.user.iam.UserProfileResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.UserService;
import com.glamora_store.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/profile")
public class UserProfileController {
  private final UserService userService;

  @GetMapping
  public ApiResponse<UserProfileResponse> getMyProfile() {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.GET_USER_SUCCESS.getMessage(),
      userService.getUserById(userId));
  }

  @PutMapping
  public ApiResponse<UserProfileResponse> updateMyProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    return new ApiResponse<>(
      SuccessMessage.UPDATE_USER_SUCCESS.getMessage(),
      userService.updateMyProfile(userId, request));
  }

  @PutMapping("/password")
  public ApiResponse<UserResponse> updateMyPassword(@Valid @RequestBody PasswordUpdateRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    userService.updatePassword(userId, request);
    return new ApiResponse<>(SuccessMessage.UPDATE_PASSWORD_SUCCESS.getMessage());
  }
}
