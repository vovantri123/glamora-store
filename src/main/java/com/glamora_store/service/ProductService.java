package com.glamora_store.service;

import com.glamora_store.dto.request.admin.product.ProductCreateRequest;
import com.glamora_store.dto.request.admin.product.ProductUpdateRequest;
import com.glamora_store.dto.response.admin.product.ProductAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductService {
  // User/Common methods
  ProductResponse getProductById(Long id);

  PageResponse<ProductResponse> searchProducts(Long categoryId, String keyword, BigDecimal minPrice,
      BigDecimal maxPrice, int page, int size, String sortBy, String sortDir);

  // Admin methods
  ProductAdminResponse createProduct(ProductCreateRequest request);

  ProductAdminResponse updateProduct(Long id, ProductUpdateRequest request);

  void deleteProduct(Long id);

  ProductAdminResponse activateProduct(Long id);

  PageResponse<ProductAdminResponse> searchProductsForAdmin(Long categoryId, String keyword, Boolean includeDeleted,
      Pageable pageable);

  ProductAdminResponse getProductByIdForAdmin(Long id);
}
