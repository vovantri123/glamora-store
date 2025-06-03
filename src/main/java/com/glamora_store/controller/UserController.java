package com.glamora_store.controller;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        ApiResponse<List<UserResponse>> response = new ApiResponse<>();
        response.setResult(userService.getAllUsers());
        return response;
    }
}
