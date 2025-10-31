package com.glamora_store.service;

import com.glamora_store.dto.request.admin.category.CategoryCreateRequest;
import com.glamora_store.dto.request.admin.category.CategoryUpdateRequest;
import com.glamora_store.dto.response.admin.category.CategoryAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.category.CategoryResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    // User/Common methods
    List<CategoryResponse> getAllCategories();

    List<CategoryResponse> getRootCategories();

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getCategoryChildren(Long parentId);

    List<CategoryResponse> getCategoryPathFromRootToCurrent(Long id);

    // Admin methods
    CategoryAdminResponse createCategory(CategoryCreateRequest request);

    CategoryAdminResponse updateCategory(Long id, CategoryUpdateRequest request);

    void deleteCategory(Long id);

    CategoryAdminResponse activateCategory(Long id);

    PageResponse<CategoryAdminResponse> searchCategories(String keyword, Boolean includeDeleted, Pageable pageable);

    CategoryAdminResponse getCategoryByIdForAdmin(Long id);
}
