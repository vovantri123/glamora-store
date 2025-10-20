package com.glamora_store.service;

import com.glamora_store.dto.request.iam.*;
import com.glamora_store.dto.response.iam.PageResponse;
import com.glamora_store.dto.response.iam.UserProfileResponse;
import com.glamora_store.dto.response.iam.UserResponse;

import java.time.LocalDate;

public interface UserService {
  void registerUser(UserCreateRequest request);

  void resetPassword(String email, String newPassword);

  UserResponse createUser(UserCreateRequest request);

  UserResponse updateUser(Long userId, UserUpdateRequest request);

  UserProfileResponse updateMyProfile(Long userId, UserProfileUpdateRequest request);

  void updatePassword(Long userId, PasswordUpdateRequest request);

  void softDeleteUser(Long userId);

  UserResponse activeUser(Long userid);

  PageResponse<UserResponse> searchUsers(
    String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir);

  UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request);

  UserProfileResponse getUserById(Long userId);

  UserProfileResponse getMyInfo();
}
