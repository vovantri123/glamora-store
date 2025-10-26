package com.glamora_store.dto.request.admin.product;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @Size(max = 255, message = "PRODUCT_NAME_TOO_LONG")
    private String name;

    @Size(max = 5000, message = "PRODUCT_DESCRIPTION_TOO_LONG")
    private String description;

    private Long categoryId;
}
