package com.glamora_store.controller.common;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.category.CategoryResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final CategoryService categoryService;

  @GetMapping
  public ApiResponse<List<CategoryResponse>> getAllCategories() {
    List<CategoryResponse> categories = categoryService.getAllCategories();
    String message = categories.isEmpty()
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_ALL_CATEGORY_SUCCESS.getMessage();
    return new ApiResponse<>(message, categories);
  }

  @GetMapping("/root")
  public ApiResponse<List<CategoryResponse>> getRootCategories() {
    List<CategoryResponse> categories = categoryService.getRootCategories();
    String message = categories.isEmpty()
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_ALL_CATEGORY_SUCCESS.getMessage();
    return new ApiResponse<>(message, categories);
  }

  /**
   * Get a single category by ID
   *
   * @param id Category ID
   * @return Single category details (and children if have)
   */
  @GetMapping("/{id}")
  public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Long id) {
    return new ApiResponse<>(
        SuccessMessage.GET_CATEGORY_SUCCESS.getMessage(),
        categoryService.getCategoryById(id));
  }

  /**
   * Get direct children of a category
   * Example: If "Fashion" has children ["Men", "Women"], returns ["Men", "Women"]
   *
   * @param id Parent category ID
   * @return List of child categories
   */
  @GetMapping("/{id}/children")
  public ApiResponse<List<CategoryResponse>> getCategoryChildren(@PathVariable Long id) {
    List<CategoryResponse> children = categoryService.getCategoryChildren(id);
    String message = children.isEmpty()
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_ALL_CATEGORY_SUCCESS.getMessage();
    return new ApiResponse<>(message, children);
  }

  /**
   * Get full category path from root to current category
   * Example: For "T-Shirts" returns ["Fashion", "Men", "T-Shirts"]
   * Used for breadcrumb navigation
   *
   * @param id Category ID
   * @return List of categories from root to current (full hierarchy path)
   */
  @GetMapping("/{id}/path")
  public ApiResponse<List<CategoryResponse>> getCategoryPathFromRootToCurrent(@PathVariable Long id) {
    List<CategoryResponse> path = categoryService.getCategoryPathFromRootToCurrent(id);
    String message = path.isEmpty()
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_CATEGORY_SUCCESS.getMessage();
    return new ApiResponse<>(message, path);
  }
}
