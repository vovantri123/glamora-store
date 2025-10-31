package com.glamora_store.dto.request.admin.product_variant;

import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantUpdateRequest {

    private String sku;

    @Min(value = 1, message = "PRODUCT_VARIANT_PRICE_INVALID")
    private BigDecimal price;

    private BigDecimal compareAtPrice;

    @Min(value = 0, message = "PRODUCT_VARIANT_STOCK_INVALID")
    private Integer stock;

    private String imageUrl; // Single image URL for this variant

    private List<Long> attributeValueIds;
}
