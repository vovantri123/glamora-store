package com.glamora_store.controller;

import com.glamora_store.dto.request.iam.*;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.iam.PageResponse;
import com.glamora_store.dto.response.iam.UserProfileResponse;
import com.glamora_store.dto.response.iam.UserResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

  private final UserService userService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    return new ApiResponse<>(SuccessMessage.CREATE_USER_SUCCESS.getMessage(), userService.createUser(request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/{userId}")
  public ApiResponse<UserResponse> updateUser(
    @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.UPDATE_USER_SUCCESS.getMessage(), userService.updateUser(userId, request));
  }

  @PreAuthorize("#id == authentication.token.claims['userId']")
  @PutMapping("/my-profile/{id}")
  public ApiResponse<UserProfileResponse> updateMyProfile(
    @PathVariable Long id, @Valid @RequestBody UserProfileUpdateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.UPDATE_USER_SUCCESS.getMessage(), userService.updateMyProfile(id, request));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{userId}")
  public ApiResponse<String> deleteUser(@PathVariable Long userId) {
    userService.softDeleteUser(userId);
    return new ApiResponse<>(SuccessMessage.DELETE_USER_SUCCESS.getMessage());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/active/{userId}")
  public ApiResponse<UserResponse> activateUser(@PathVariable Long userId) {
    return new ApiResponse<>(SuccessMessage.ACTIVATE_USER_SUCCESS.getMessage(), userService.activeUser(userId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ApiResponse<PageResponse<UserResponse>> searchUsers(
    @RequestParam(required = false) String fullname,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "4") int size,
    @RequestParam(defaultValue = "userId") String sortBy,
    @RequestParam(defaultValue = "desc") String sortDir) {

    PageResponse<UserResponse> result = userService.searchUsers(fullname, dob, page, size, sortBy, sortDir);

    String message = (result.getContent().isEmpty())
      ? SuccessMessage.NO_DATA_FOUND.getMessage()
      : SuccessMessage.SEARCH_USER_SUCCESS.getMessage();

    return new ApiResponse<>(message, result);
  }

  // authentication.name là sub claim (email)
  @PostAuthorize("hasRole('ADMIN') or returnObject.data.email == authentication.name")
  @GetMapping("/{userId}")
  public ApiResponse<UserProfileResponse> getUserById(@PathVariable Long userId) {
    return new ApiResponse<>(SuccessMessage.GET_USER_SUCCESS.getMessage(), userService.getUserById(userId));
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/roles/{userId}")
  public ApiResponse<UserResponse> updateRolesForUser(
    @PathVariable Long userId, @Valid @RequestBody UserRoleUpdateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.UPDATE_ROLE_OF_USER_SUCCESS.getMessage(),
      userService.updateRolesForUser(userId, request));
  }

  @PreAuthorize("hasRole('ADMIN') or #userId == authentication.token.claims['userId']")
  @PutMapping("/updatePassword/{userId}")
  public ApiResponse<UserResponse> updatePassword(
    @PathVariable Long userId, @Valid @RequestBody PasswordUpdateRequest request) {
    userService.updatePassword(userId, request);
    return new ApiResponse<>(SuccessMessage.UPDATE_PASSWORD_SUCCESS.getMessage());
  }
}
