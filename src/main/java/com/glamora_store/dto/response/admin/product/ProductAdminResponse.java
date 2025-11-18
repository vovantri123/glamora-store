package com.glamora_store.dto.response.admin.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdminResponse {

  private Long id;

  private String name;

  private String description;

  private String thumbnailUrl;

  private Long categoryId;

  private String categoryName;

  private Boolean isDeleted;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String createdBy;

  private String updatedBy;
}
