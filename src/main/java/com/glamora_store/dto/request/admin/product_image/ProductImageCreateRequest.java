package com.glamora_store.dto.request.admin.product_image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageCreateRequest {

    @NotBlank(message = "PRODUCT_IMAGE_URL_REQUIRED")
    private String imageUrl;

    private String altText;

    private Boolean isThumbnail;

    private Integer displayOrder;

    @NotNull(message = "PRODUCT_IMAGE_PRODUCT_ID_REQUIRED")
    private Long productId;

    private Long variantId;
}
