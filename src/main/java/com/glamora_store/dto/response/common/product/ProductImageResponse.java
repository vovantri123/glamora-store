package com.glamora_store.dto.response.common.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {
    private Long id;
    private String imageUrl;
    private String altText;
    private Boolean isThumbnail;
    private Integer displayOrder;
}
