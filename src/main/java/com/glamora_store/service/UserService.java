package com.glamora_store.service;

import java.time.LocalDate;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import com.glamora_store.dto.request.*;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserProfileResponse;
import com.glamora_store.dto.response.UserResponse;

public interface UserService {
    void registerUser(UserCreateRequest request);

    void resetPassword(String email, String newPassword);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse createUser(UserCreateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse updateUser(Long userId, UserUpdateRequest request);

    // authentication.name là sub claim
    @PreAuthorize("#email == authentication.name")
    UserProfileResponse updateMyProfile(String email, UserProfileUpdateRequest request);

    @PreAuthorize("hasRole('ADMIN') or #email == authentication.name")
    void updatePassword(String email, PasswordUpdateRequest request);

    @PreAuthorize("hasRole('ADMIN')")
    void softDeleteUser(Long userId);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse activeUser(Long userid);

    @PreAuthorize("hasRole('ADMIN')")
    PageResponse<UserResponse> searchUsers(
            String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir);

    @PreAuthorize("hasRole('ADMIN')")
    UserResponse updateRolesForUser(Long userId, UserRoleUpdateRequest request);

    @PostAuthorize("returnObject.email == authentication.name")
    UserProfileResponse getUserById(Long userId);

    @PreAuthorize("hasAuthority('USER_READ')")
    UserProfileResponse getMyInfo();
}
