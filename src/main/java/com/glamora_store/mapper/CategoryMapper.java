package com.glamora_store.mapper;

import com.glamora_store.dto.request.admin.category.CategoryCreateRequest;
import com.glamora_store.dto.request.admin.category.CategoryUpdateRequest;
import com.glamora_store.dto.response.admin.category.CategoryAdminResponse;
import com.glamora_store.dto.response.common.category.CategoryResponse;
import com.glamora_store.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    @Mapping(target = "productCount", expression = "java(category.getProducts().size())")
    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toCategoryResponseList(List<Category> categories);

    @AfterMapping
    default void mapChildren(@MappingTarget CategoryResponse response, Category category) {
        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            response.setChildren(toCategoryResponseList(category.getChildren().stream().toList()));
        }
    }

    // Admin mappings
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "parentName", source = "parent.name")
    CategoryAdminResponse toCategoryAdminResponse(Category category);

    List<CategoryAdminResponse> toCategoryAdminResponseList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    Category toCategory(CategoryCreateRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "children", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateCategoryFromRequest(CategoryUpdateRequest request, @MappingTarget Category category);
}
