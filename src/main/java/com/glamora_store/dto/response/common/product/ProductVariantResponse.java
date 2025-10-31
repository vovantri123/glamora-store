package com.glamora_store.dto.response.common.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {
    private Long id;
    private String sku;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private Integer stock;
    private String imageUrl;
    private List<VariantAttributeResponse> attributes;
}
