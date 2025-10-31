package com.glamora_store.service;

import com.glamora_store.dto.request.admin.product_image.ProductImageCreateRequest;
import com.glamora_store.dto.request.admin.product_image.ProductImageUpdateRequest;
import com.glamora_store.dto.response.admin.product_image.ProductImageAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import org.springframework.data.domain.Pageable;

public interface ProductImageService {

    ProductImageAdminResponse createProductImage(ProductImageCreateRequest request);

    ProductImageAdminResponse updateProductImage(Long id, ProductImageUpdateRequest request);

    void deleteProductImage(Long id);

    ProductImageAdminResponse getProductImageById(Long id);

    PageResponse<ProductImageAdminResponse> getAllProductImages(Long productId, Pageable pageable);
}
