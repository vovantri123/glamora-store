package com.glamora_store.mapper;

import com.glamora_store.dto.request.UserCreationRequest;
import com.glamora_store.dto.request.UserUpdateRequest;
import com.glamora_store.dto.response.CategoryResponse;
import com.glamora_store.dto.response.SubCategoryResponse;
import com.glamora_store.dto.response.UserResponse;
import com.glamora_store.entity.Category;
import com.glamora_store.entity.SubCategory;
import com.glamora_store.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toListCategoryResponse(List<Category> categories);
}