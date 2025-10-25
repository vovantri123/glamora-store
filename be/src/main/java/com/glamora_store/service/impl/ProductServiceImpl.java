package com.glamora_store.service.impl;

import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;
import com.glamora_store.entity.Product;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.ProductMapper;
import com.glamora_store.repository.ProductRepository;
import com.glamora_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

  @Override
  @Transactional(readOnly = true)
  public ProductResponse getProductById(Long id) {
    Product product = productRepository.findByIdAndIsDeletedFalse(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));
    return productMapper.toProductResponse(product);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductResponse> searchProducts(Long categoryId, String keyword, int page, int size,
      String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Product> productPage = productRepository.searchProducts(categoryId, keyword, pageable);

    return PageResponse.<ProductResponse>builder()
        .content(productMapper.toProductResponseList(productPage.getContent()))
        .pageNumber(productPage.getNumber())
        .pageSize(productPage.getSize())
        .numberOfElements(productPage.getNumberOfElements())
        .totalElements(productPage.getTotalElements())
        .totalPages(productPage.getTotalPages())
        .last(productPage.isLast())
        .build();
  }
}
