package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.product_image.ProductImageCreateRequest;
import com.glamora_store.dto.request.admin.product_image.ProductImageUpdateRequest;
import com.glamora_store.dto.response.admin.product_image.ProductImageAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.entity.Product;
import com.glamora_store.entity.ProductImage;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.ProductImageMapper;
import com.glamora_store.repository.ProductImageRepository;
import com.glamora_store.repository.ProductRepository;
import com.glamora_store.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

  private final ProductImageRepository productImageRepository;
  private final ProductRepository productRepository;
  private final ProductImageMapper productImageMapper;

  @Override
  @Transactional
  public ProductImageAdminResponse createProductImage(ProductImageCreateRequest request) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    // If this image should be thumbnail, unset current thumbnail
    if (Boolean.TRUE.equals(request.getIsThumbnail())) {
      productImageRepository.findByProductIdAndIsThumbnailTrue(product.getId())
          .ifPresent(currentThumbnail -> {
            currentThumbnail.setIsThumbnail(false);
            productImageRepository.save(currentThumbnail);
          });
    }

    ProductImage productImage = productImageMapper.toProductImage(request);
    productImage.setProduct(product);

    ProductImage savedImage = productImageRepository.save(productImage);
    return productImageMapper.toProductImageAdminResponse(savedImage);
  }

  @Override
  @Transactional
  public ProductImageAdminResponse updateProductImage(Long id, ProductImageUpdateRequest request) {
    ProductImage productImage = productImageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_IMAGE_NOT_FOUND.getMessage()));

    productImageMapper.updateProductImageFromRequest(request, productImage);

    ProductImage updatedImage = productImageRepository.save(productImage);
    return productImageMapper.toProductImageAdminResponse(updatedImage);
  }

  @Override
  @Transactional
  public void deleteProductImage(Long id) {
    ProductImage productImage = productImageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_IMAGE_NOT_FOUND.getMessage()));
    productImageRepository.delete(productImage);
  }

  @Override
  public ProductImageAdminResponse getProductImageById(Long id) {
    ProductImage productImage = productImageRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_IMAGE_NOT_FOUND.getMessage()));
    return productImageMapper.toProductImageAdminResponse(productImage);
  }

  @Override
  public PageResponse<ProductImageAdminResponse> getAllProductImages(Long productId, Boolean isDeleted,
      Pageable pageable) {
    Page<ProductImage> productImages;

    if (productId != null) {
      productImages = productImageRepository.findByProductId(productId, pageable);
    } else {
      productImages = productImageRepository.findAll(pageable);
    }

    return PageResponse.from(productImages.map(productImageMapper::toProductImageAdminResponse));
  }

  @Override
  @Transactional
  public ProductImageAdminResponse setAsThumbnail(Long imageId) {
    ProductImage productImage = productImageRepository.findById(imageId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_IMAGE_NOT_FOUND.getMessage()));

    Product product = productImage.getProduct();

    // Unset current thumbnail
    productImageRepository.findByProductIdAndIsThumbnailTrue(product.getId())
        .ifPresent(currentThumbnail -> {
          currentThumbnail.setIsThumbnail(false);
          productImageRepository.save(currentThumbnail);
        });

    // Set new thumbnail
    productImage.setIsThumbnail(true);
    ProductImage updatedImage = productImageRepository.save(productImage);

    return productImageMapper.toProductImageAdminResponse(updatedImage);
  }
}
