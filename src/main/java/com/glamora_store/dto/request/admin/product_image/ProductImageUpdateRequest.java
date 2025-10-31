package com.glamora_store.dto.request.admin.product_image;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageUpdateRequest {

    private String imageUrl;

    private String altText;

    private Boolean isThumbnail;

    private Integer displayOrder;
}
