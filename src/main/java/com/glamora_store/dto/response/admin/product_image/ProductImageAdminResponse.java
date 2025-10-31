package com.glamora_store.dto.response.admin.product_image;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageAdminResponse {

    private Long id;
    private String imageUrl;
    private String altText;
    private Boolean isThumbnail;
    private Integer displayOrder;
    private Long productId;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
