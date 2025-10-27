package com.glamora_store.dto.request.admin.product_variant;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class ProductVariantCreateRequest {

    // SKU is now optional - will be auto-generated if not provided
    // Format: GLM-{5-DIGIT} (e.g., GLM-00001, GLM-00002)
    // GLM = GLaMora brand identifier
    private String sku;

    @NotNull(message = "PRODUCT_VARIANT_PRICE_REQUIRED")
    @Min(value = 1, message = "PRODUCT_VARIANT_PRICE_INVALID")
    private BigDecimal price;

    private BigDecimal compareAtPrice;

    @NotNull(message = "PRODUCT_VARIANT_STOCK_INVALID")
    @Min(value = 0, message = "PRODUCT_VARIANT_STOCK_INVALID")
    private Integer stock;

    @NotNull(message = "PRODUCT_VARIANT_PRODUCT_ID_REQUIRED")
    private Long productId;

    private List<Long> attributeValueIds;
}
