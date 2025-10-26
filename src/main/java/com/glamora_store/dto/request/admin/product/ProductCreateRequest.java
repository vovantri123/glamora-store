package com.glamora_store.dto.request.admin.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

    @NotBlank(message = "PRODUCT_NAME_REQUIRED")
    @Size(max = 255, message = "PRODUCT_NAME_TOO_LONG")
    private String name;

    @Size(max = 5000, message = "PRODUCT_DESCRIPTION_TOO_LONG")
    private String description;

    @NotNull(message = "PRODUCT_CATEGORY_ID_REQUIRED")
    private Long categoryId;
}
