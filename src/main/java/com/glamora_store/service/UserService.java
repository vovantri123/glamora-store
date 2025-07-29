package com.glamora_store.service;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.PageResponse;
import com.glamora_store.dto.response.UserResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

  UserResponse createUser(UserCreationRequest request);

  UserResponse updateUser(Long userId, UserUpdateRequest request);

  void softDeleteUser(Long userId);

  public UserResponse activeUser(Long userid);

  PageResponse<UserResponse> searchUsers(
      String fullname, LocalDate dob, int page, int size, String sortBy, String sortDir);

  UserResponse getUserById(Long userId);
}
