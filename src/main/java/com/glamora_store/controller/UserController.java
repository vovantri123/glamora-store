package com.glamora_store.controller;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setResult(userService.getAllUsers());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.getUserById(id));
        return response;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.createUser(request));
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setResult(userService.updateUser(id, request));
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<String> response = new ApiResponse<>();
        response.setResult("User has been deleted");
        return response;
    }

}
