package com.glamora_store.dto.response;

import com.glamora_store.entity.SubCategory;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private Set<SubCategoryResponse> subCategories;
}
