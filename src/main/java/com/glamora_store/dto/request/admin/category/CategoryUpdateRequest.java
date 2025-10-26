package com.glamora_store.dto.request.admin.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryUpdateRequest {

    @Size(max = 255, message = "CATEGORY_NAME_TOO_LONG")
    private String name;

    @Size(max = 1000, message = "CATEGORY_DESCRIPTION_TOO_LONG")
    private String description;

    private String imageUrl;

    private Long parentId;
}
