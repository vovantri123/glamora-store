package com.glamora_store.dto.response.common.product;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String thumbnailUrl;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> variants;
    private Integer totalStock;
    private LocalDateTime createdAt;
}
