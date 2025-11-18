package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.product.ProductCreateRequest;
import com.glamora_store.dto.request.admin.product.ProductUpdateRequest;
import com.glamora_store.dto.response.admin.product.ProductAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;
import com.glamora_store.entity.Category;
import com.glamora_store.entity.Product;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.ProductMapper;
import com.glamora_store.repository.CategoryRepository;
import com.glamora_store.repository.ProductRepository;
import com.glamora_store.service.ProductService;
import com.glamora_store.util.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

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
  public PageResponse<ProductResponse> searchProducts(Long categoryId, String keyword, BigDecimal minPrice,
      BigDecimal maxPrice, int page, int size, String sortBy, String sortDir) {
    Sort sort = sortDir.equalsIgnoreCase("asc")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Product> productPage = productRepository.searchProducts(categoryId, keyword, minPrice, maxPrice, pageable);

    return PageResponse.from(productPage.map(productMapper::toProductResponse));
  }

  // Admin methods implementation
  @Override
  @Transactional
  public ProductAdminResponse createProduct(ProductCreateRequest request) {
    // Check if product name already exists
    if (productRepository.existsByName(request.getName())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          ErrorMessage.PRODUCT_NAME_ALREADY_EXISTS.getMessage());
    }

    // Verify category exists
    Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));

    Product product = productMapper.toProduct(request);
    product.setCategory(category);
    product.setIsDeleted(false);

    Product savedProduct = productRepository.save(product);
    return productMapper.toProductAdminResponse(savedProduct);
  }

  @Override
  @Transactional
  public ProductAdminResponse updateProduct(Long id, ProductUpdateRequest request) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    // Check if new name already exists (excluding current product)
    if (request.getName() != null && !request.getName().equals(product.getName())) {
      if (productRepository.existsByName(request.getName())) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            ErrorMessage.PRODUCT_NAME_ALREADY_EXISTS.getMessage());
      }
    }

    productMapper.updateProductFromRequest(request, product);

    // Update category if categoryId is provided
    if (request.getCategoryId() != null) {
      Category category = categoryRepository.findByIdAndIsDeletedFalse(request.getCategoryId())
          .orElseThrow(() -> new ResponseStatusException(
              HttpStatus.NOT_FOUND,
              ErrorMessage.CATEGORY_NOT_FOUND.getMessage()));
      product.setCategory(category);
    }

    Product updatedProduct = productRepository.save(product);
    return productMapper.toProductAdminResponse(updatedProduct);
  }

  @Override
  @Transactional
  public void deleteProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    // Soft delete
    product.setIsDeleted(true);
    productRepository.save(product);
  }

  @Override
  @Transactional
  public ProductAdminResponse activateProduct(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    product.setIsDeleted(false);
    Product activatedProduct = productRepository.save(product);
    return productMapper.toProductAdminResponse(activatedProduct);
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<ProductAdminResponse> searchProductsForAdmin(Long categoryId, String keyword,
      Boolean isDeleted, Pageable pageable) {
    Specification<Product> spec = isDeleted
        ? ProductSpecification.isDeleted()
            .and(ProductSpecification.hasCategoryId(categoryId))
            .and(ProductSpecification.hasNameLike(keyword))
        : ProductSpecification.isNotDeleted()
            .and(ProductSpecification.hasCategoryId(categoryId))
            .and(ProductSpecification.hasNameLike(keyword));

    Page<Product> productPage = productRepository.findAll(spec, pageable);

    return PageResponse.from(productPage.map(productMapper::toProductAdminResponse));
  }

  @Override
  @Transactional(readOnly = true)
  public ProductAdminResponse getProductByIdForAdmin(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));
    return productMapper.toProductAdminResponse(product);
  }
}
