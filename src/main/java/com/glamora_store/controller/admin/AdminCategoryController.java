package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.category.CategoryCreateRequest;
import com.glamora_store.dto.request.admin.category.CategoryUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.category.CategoryAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các API quản lý categories cho admin
 * Admin có thể CRUD tất cả categories trong hệ thống
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ApiResponse<CategoryAdminResponse> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        return new ApiResponse<>(
                SuccessMessage.CREATE_CATEGORY_SUCCESS.getMessage(),
                categoryService.createCategory(request));
    }

    @PutMapping("/{categoryId}")
    public ApiResponse<CategoryAdminResponse> updateCategory(
            @PathVariable Long categoryId,
            @Valid @RequestBody CategoryUpdateRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATE_CATEGORY_SUCCESS.getMessage(),
                categoryService.updateCategory(categoryId, request));
    }

    @DeleteMapping("/{categoryId}")
    public ApiResponse<String> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ApiResponse<>(SuccessMessage.DELETE_CATEGORY_SUCCESS.getMessage());
    }

    @PutMapping("/{categoryId}/activate")
    public ApiResponse<CategoryAdminResponse> activateCategory(@PathVariable Long categoryId) {
        return new ApiResponse<>(
                SuccessMessage.ACTIVATE_CATEGORY_SUCCESS.getMessage(),
                categoryService.activateCategory(categoryId));
    }

    @GetMapping
    public ApiResponse<PageResponse<CategoryAdminResponse>> searchCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "false") boolean includeDeleted,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<CategoryAdminResponse> result = categoryService.searchCategories(keyword, includeDeleted,
                pageable);

        String message = (result.getContent().isEmpty())
                ? SuccessMessage.NO_DATA_FOUND.getMessage()
                : SuccessMessage.GET_ALL_CATEGORY_SUCCESS.getMessage();

        return new ApiResponse<>(message, result);
    }

    @GetMapping("/{categoryId}")
    public ApiResponse<CategoryAdminResponse> getCategoryById(@PathVariable Long categoryId) {
        return new ApiResponse<>(
                SuccessMessage.GET_CATEGORY_SUCCESS.getMessage(),
                categoryService.getCategoryByIdForAdmin(categoryId));
    }
}
