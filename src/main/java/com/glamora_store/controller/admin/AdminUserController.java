package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.iam.UserCreateRequest;
import com.glamora_store.dto.request.admin.iam.UserRoleUpdateRequest;
import com.glamora_store.dto.request.admin.iam.UserUpdateRequest;
import com.glamora_store.dto.request.common.iam.PasswordUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.iam.UserResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.user.iam.UserProfileResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller xử lý các API quản lý users cho admin
 * Admin có thể CRUD tất cả users trong hệ thống
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
  private final UserService userService;

  @PostMapping
  public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.CREATE_USER_SUCCESS.getMessage(),
        userService.createUser(request));
  }

  @PutMapping("/{userId}")
  public ApiResponse<UserResponse> updateUser(
      @PathVariable Long userId,
      @Valid @RequestBody UserUpdateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.UPDATE_USER_SUCCESS.getMessage(),
        userService.updateUser(userId, request));
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<String> deleteUser(@PathVariable Long userId) {
    userService.softDeleteUser(userId);
    return new ApiResponse<>(SuccessMessage.DELETE_USER_SUCCESS.getMessage());
  }

  @PutMapping("/{userId}/activate")
  public ApiResponse<UserResponse> activateUser(@PathVariable Long userId) {
    return new ApiResponse<>(
        SuccessMessage.ACTIVATE_USER_SUCCESS.getMessage(),
        userService.activeUser(userId));
  }

  @GetMapping
  public ApiResponse<PageResponse<UserResponse>> searchUsers(
      @RequestParam(required = false) String fullname,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
      @RequestParam(defaultValue = "false") boolean includeDeleted,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size,
      @RequestParam(defaultValue = "id") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    Page<UserResponse> pageResult = userService.searchUsers(fullname, dob, includeDeleted, pageable);
    PageResponse<UserResponse> result = PageResponse.from(pageResult);

    String message = (result.getContent().isEmpty())
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.SEARCH_USER_SUCCESS.getMessage();

    return new ApiResponse<>(message, result);
  }

  @GetMapping("/{userId}")
  public ApiResponse<UserProfileResponse> getUserById(@PathVariable Long userId) {
    return new ApiResponse<>(
        SuccessMessage.GET_USER_SUCCESS.getMessage(),
        userService.getUserById(userId));
  }

  @PutMapping("/{userId}/roles")
  public ApiResponse<UserResponse> updateRolesForUser(
      @PathVariable Long userId,
      @Valid @RequestBody UserRoleUpdateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.UPDATE_ROLE_OF_USER_SUCCESS.getMessage(),
        userService.updateRolesForUser(userId, request));
  }

  @PutMapping("/{userId}/password")
  public ApiResponse<UserResponse> updatePasswordForUser(
      @PathVariable Long userId,
      @Valid @RequestBody PasswordUpdateRequest request) {
    // Cái Service dùng chung User là k ổn, Admin k cần nhập mật khẩu cũ
    userService.updatePassword(userId, request);
    return new ApiResponse<>(SuccessMessage.UPDATE_PASSWORD_SUCCESS.getMessage());
  }
}
