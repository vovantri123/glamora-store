package com.glamora_store.service;

import com.glamora_store.dto.request.iam.*;
import com.glamora_store.dto.response.iam.PageResponse;
import com.glamora_store.dto.response.iam.UserProfileResponse;
import com.glamora_store.dto.response.iam.UserResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;

public interface UserService {
  void registerUser(UserCreateRequest request);

  void resetPassword(String email, String newPassword);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse createUser(UserCreateRequest request);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateUser(Long userId, UserUpdateRequest request);

  @PreAuthorize("#userId == authentication.token.claims['userId']")
  UserProfileResponse updateMyProfile(Long userId, UserProfileUpdateRequest request);

  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.token.claims['userId']")
  void updatePassword(Long userId, PasswordUpdateRequest request);

  @PreAuthorize("hasRole('ADMIN')")
  void softDeleteUser(Long userId);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse activeUser(Long userid);

  @PreAuthorize("hasRole('ADMIN')")
  PageResponse<UserResponse> searchUsers(
    String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request);

  // authentication.name là sub claim (email)
  @PostAuthorize("hasRole('ADMIN') or returnObject.email == authentication.name")
  UserProfileResponse getUserById(Long userId);

  @PreAuthorize("hasAuthority('USER_READ')")
  UserProfileResponse getMyInfo();
}
