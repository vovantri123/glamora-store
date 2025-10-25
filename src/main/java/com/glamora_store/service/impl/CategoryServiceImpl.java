package com.glamora_store.service.impl;

import com.glamora_store.dto.response.common.category.CategoryResponse;
import com.glamora_store.entity.Category;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.CategoryMapper;
import com.glamora_store.repository.CategoryRepository;
import com.glamora_store.service.CategoryService;
import lombok.RequiredArgsConstructor;
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
}
