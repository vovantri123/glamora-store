package com.glamora_store.service;

import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
}
