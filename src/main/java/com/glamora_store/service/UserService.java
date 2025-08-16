package com.glamora_store.service;

import com.glamora_store.dto.request.UserCreateRequest;
import com.glamora_store.dto.request.UserRoleUpdateRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserResponse;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDate;

public interface UserService {

  UserResponse createUser(UserCreateRequest request);

  UserResponse updateUser(Long userId, UserUpdateRequest request);

  void softDeleteUser(Long userId);

  public UserResponse activeUser(Long userid);

  @PreAuthorize("hasRole('ADMIN')")
  PageResponse<UserResponse> searchUsers(
    String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir);

  // authentication.name là sub claim
  @PostAuthorize("returnObject.email == authentication.name")
  UserResponse getUserById(Long userId);

  @PreAuthorize("hasRole('ADMIN')")
  UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request);

  UserResponse getMyInfo();
}
