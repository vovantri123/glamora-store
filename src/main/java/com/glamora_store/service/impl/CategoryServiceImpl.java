package com.glamora_store.service.impl;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.CategoryResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.User;
import com.glamora_store.mapper.CategoryMapper;
import com.glamora_store.mapper.UserMapper;
import com.glamora_store.repository.CategoryRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.CategoryService;
import com.glamora_store.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryMapper.toListCategoryResponse(categoryRepository.findAll());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found")));
    }
}