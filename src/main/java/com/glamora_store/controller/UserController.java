package com.glamora_store.controller;

import java.time.LocalDate;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.glamora_store.dto.request.*;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserProfileResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.UserService;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        return new ApiResponse<>(SuccessMessage.CREATE_USER_SUCCESS.getMessage(), userService.createUser(request));
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(
            @PathVariable Long userId, @Valid @RequestBody UserUpdateRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATE_USER_SUCCESS.getMessage(), userService.updateUser(userId, request));
    }

    @PutMapping("/my-profile/{email}")
    public ApiResponse<UserProfileResponse> updateUser(
            @PathVariable String email, @Valid @RequestBody UserProfileUpdateRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATE_USER_SUCCESS.getMessage(), userService.updateMyProfile(email, request));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<String> deleteUser(@PathVariable Long userId) {
        userService.softDeleteUser(userId);
        return new ApiResponse<>(SuccessMessage.DELETE_USER_SUCCESS.getMessage());
    }

    @PutMapping("/active/{userId}")
    public ApiResponse<UserResponse> activateUser(@PathVariable Long userId) {
        return new ApiResponse<>(SuccessMessage.ACTIVATE_USER_SUCCESS.getMessage(), userService.activeUser(userId));
    }

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

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUser(@PathVariable Long userId) {
        return new ApiResponse<>(SuccessMessage.GET_USER_SUCCESS.getMessage(), userService.getUserById(userId));
    }

    @GetMapping("/myInfo")
    public ApiResponse<UserProfileResponse> getMyInfo() {
        return new ApiResponse<>(SuccessMessage.THIS_IS_YOUR_INFO.getMessage(), userService.getMyInfo());
    }

    @PutMapping("/roles/{userId}")
    public ApiResponse<UserResponse> updateRolesForUser(
            @PathVariable Long userId, @RequestBody UserRoleUpdateRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATE_ROLE_OF_USER_SUCCESS.getMessage(), userService.updateRolesForUser(userId, request));
    }

    @PutMapping("/updatePassword/{email}")
    public ApiResponse<UserResponse> updatePassword(
            @PathVariable String email, @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(email, request);
        return new ApiResponse<>(SuccessMessage.UPDATE_PASSWORD_SUCCESS.getMessage());
    }
}
