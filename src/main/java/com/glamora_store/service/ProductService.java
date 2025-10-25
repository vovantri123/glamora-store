package com.glamora_store.service;

import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;

public interface ProductService {
  ProductResponse getProductById(Long id);

  PageResponse<ProductResponse> searchProducts(Long categoryId, String keyword, int page, int size, String sortBy,
                                               String sortDir);
}
