package com.glamora_store.dto.response.admin.product_variant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantAdminResponse {

    private Long id;
    private String sku;
    private BigDecimal price;
    private BigDecimal compareAtPrice;
    private Integer stock;
    private String imageUrl;
    private Boolean isDeleted;
    private Long productId;
    private String productName;
    private List<AttributeValueInfo> variantValues;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttributeValueInfo {
        private Long attributeValueId;
        private String attributeName;
        private String valueName;
    }
}
