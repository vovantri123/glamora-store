package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.category.CategoryCreateRequest;
import com.glamora_store.dto.request.admin.category.CategoryUpdateRequest;
import com.glamora_store.dto.response.admin.category.CategoryAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.category.CategoryResponse;
import com.glamora_store.entity.Category;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.CategoryMapper;
import com.glamora_store.repository.CategoryRepository;
import com.glamora_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> getAllCategories() {
    List<Category> categories = categoryRepository.findByIsDeletedFalse();
    return categoryMapper.toCategoryResponseList(categories);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> getRootCategories() {
    List<Category> rootCategories = categoryRepository.findByParentIsNullAndIsDeletedFalse();
    return categoryMapper.toCategoryResponseList(rootCategories);
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponse getCategoryById(Long id) {
    Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));
    return categoryMapper.toCategoryResponse(category);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> getCategoryChildren(Long parentId) {
    List<Category> children = categoryRepository.findByParentIdAndIsDeletedFalse(parentId);
    return categoryMapper.toCategoryResponseList(children);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponse> getCategoryPathFromRootToCurrent(Long id) {
    Category category = categoryRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));

    List<CategoryResponse> path = new java.util.ArrayList<>();
    Category current = category;

    // Build path from current to root
    while (current != null) {
      path.add(0, categoryMapper.toCategoryResponse(current)); // Add 0 to insert at the beginning
      current = current.getParent();
    }

    return path;
  }

  // Admin methods implementation
  @Override
  @Transactional
  public CategoryAdminResponse createCategory(CategoryCreateRequest request) {
    // Check if category name already exists
    if (categoryRepository.existsByName(request.getName())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          ErrorMessage.CATEGORY_NAME_ALREADY_EXISTS.getMessage());
    }

    Category category = categoryMapper.toCategory(request);
    category.setIsDeleted(false);

    // Set parent if parentId is provided
    if (request.getParentId() != null) {
      Category parent = categoryRepository.findById(request.getParentId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));
      category.setParent(parent);
    }

    Category savedCategory = categoryRepository.save(category);
    return categoryMapper.toCategoryAdminResponse(savedCategory);
  }

  @Override
  @Transactional
  public CategoryAdminResponse updateCategory(Long id, CategoryUpdateRequest request) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));

    // Check if new name already exists (excluding current category)
    if (request.getName() != null && !request.getName().equals(category.getName())) {
      if (categoryRepository.existsByName(request.getName())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            ErrorMessage.CATEGORY_NAME_ALREADY_EXISTS.getMessage());
      }
    }

    categoryMapper.updateCategoryFromRequest(request, category);

    // Update parent if parentId is provided
    if (request.getParentId() != null) {
      // Prevent setting itself as parent
      if (request.getParentId().equals(id)) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            ErrorMessage.CATEGORY_CANNOT_BE_OWN_PARENT.getMessage());
      }
      Category parent = categoryRepository.findById(request.getParentId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));
      category.setParent(parent);
    }

    Category updatedCategory = categoryRepository.save(category);
    return categoryMapper.toCategoryAdminResponse(updatedCategory);
  }

  @Override
  @Transactional
  public void deleteCategory(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));

    // Check if category has products
    if (!category.getProducts().isEmpty()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          ErrorMessage.CATEGORY_CANNOT_DELETE_WITH_PRODUCTS.getMessage());
    }

    // Soft delete
    category.setIsDeleted(true);
    categoryRepository.save(category);
  }

  @Override
  @Transactional
  public CategoryAdminResponse activateCategory(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));

    category.setIsDeleted(false);
    Category activatedCategory = categoryRepository.save(category);
    return categoryMapper.toCategoryAdminResponse(activatedCategory);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<CategoryAdminResponse> searchCategories(String keyword, Boolean includeDeleted,
      Pageable pageable) {
    Page<Category> categoryPage = categoryRepository.searchCategories(keyword, includeDeleted, pageable);

    return PageResponse.from(categoryPage.map(categoryMapper::toCategoryAdminResponse));
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryAdminResponse getCategoryByIdForAdmin(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));
    return categoryMapper.toCategoryAdminResponse(category);
  }
}
