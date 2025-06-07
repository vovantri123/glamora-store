package com.glamora_store.service;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.CategoryResponse;
import com.glamora_store.dto.response.UserResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryById(Long id);
}
