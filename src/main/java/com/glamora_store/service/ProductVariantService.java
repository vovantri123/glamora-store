package com.glamora_store.service;

import com.glamora_store.dto.request.admin.product_variant.ProductVariantCreateRequest;
import com.glamora_store.dto.request.admin.product_variant.ProductVariantUpdateRequest;
import com.glamora_store.dto.response.admin.product_variant.ProductVariantAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ProductVariantService {

    ProductVariantAdminResponse createProductVariant(ProductVariantCreateRequest request);

    ProductVariantAdminResponse updateProductVariant(Long id, ProductVariantUpdateRequest request);

    void deleteProductVariant(Long id);

    ProductVariantAdminResponse activateProductVariant(Long id);

    ProductVariantAdminResponse getProductVariantById(Long id);

    PageResponse<ProductVariantAdminResponse> searchProductVariants(Long productId, String keyword,
            Boolean includeDeleted, Pageable pageable);
}
