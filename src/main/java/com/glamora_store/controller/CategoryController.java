package com.glamora_store.controller;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.CategoryResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.mapper.CategoryMapper;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.service.CategoryService;
import com.glamora_store.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        ApiResponse<List<CategoryResponse>> response = new ApiResponse<>();
        response.setResult(categoryService.getAllCategories());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getUser(@PathVariable Long id) {
        ApiResponse<CategoryResponse> response = new ApiResponse<>();
        response.setResult(categoryService.getCategoryById(id));
        return response;
    }

}
