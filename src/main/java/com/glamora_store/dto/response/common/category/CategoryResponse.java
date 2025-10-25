package com.glamora_store.dto.response.common.category;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Long parentId;
    private String parentName;
    private List<CategoryResponse> children;
    private Integer productCount;
    private LocalDateTime createdAt;
}
