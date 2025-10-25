package com.glamora_store.service;

import com.glamora_store.dto.response.common.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getRootCategories();

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getCategoryChildren(Long parentId);
}
