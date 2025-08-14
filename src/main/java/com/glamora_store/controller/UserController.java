package com.glamora_store.controller;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.UserService;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreationRequest request) {
    return new ApiResponse<>(
      HttpStatus.CREATED.value(),
      SuccessMessage.CREATE_USER_SUCCESS.getMessage(),
      userService.createUser(request));
  }

  @PutMapping("/{userId}")
  public ApiResponse<UserResponse> updateUser(
    @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest request) {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.UPDATE_USER_SUCCESS.getMessage(),
      userService.updateUser(userId, request));
  }

  @DeleteMapping("/{userId}")
  public ApiResponse<String> deleteUser(@PathVariable Long userId) {
    userService.softDeleteUser(userId);
    return new ApiResponse<>(
      HttpStatus.OK.value(), SuccessMessage.DELETE_USER_SUCCESS.getMessage());
  }

  @PutMapping("/active/{userId}")
  public ApiResponse<UserResponse> activateUser(@PathVariable Long userId) {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.ACTIVATE_USER_SUCCESS.getMessage(),
      userService.activeUser(userId));
  }

  @GetMapping
  public ApiResponse<PageResponse<UserResponse>> searchUsers(
    @RequestParam(required = false) String fullname,
    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "4") int size,
    @RequestParam(defaultValue = "userId") String sortBy,
    @RequestParam(defaultValue = "desc") String sortDir) {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.SEARCH_USER_SUCCESS.getMessage(),
      userService.searchUsers(fullname, dob, page, size, sortBy, sortDir));
  }

  @GetMapping("/{userId}")
  public ApiResponse<UserResponse> getUser(@PathVariable Long userId) {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.GET_USER_SUCCESS.getMessage(),
      userService.getUserById(userId));
  }

  @GetMapping("/myInfo")
  public ApiResponse<UserResponse> getMyInfo() {
    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.THIS_IS_MY_INFO.getMessage(),
      userService.getMyInfo()
    );
  }
}
