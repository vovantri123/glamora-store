package com.glamora_store.mapper;

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
}
