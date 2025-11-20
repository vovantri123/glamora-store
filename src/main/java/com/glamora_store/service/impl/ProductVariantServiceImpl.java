package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.product_variant.ProductVariantCreateRequest;
import com.glamora_store.dto.request.admin.product_variant.ProductVariantUpdateRequest;
import com.glamora_store.dto.response.admin.product_variant.ProductVariantAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.entity.AttributeValue;
import com.glamora_store.entity.Product;
import com.glamora_store.entity.ProductVariant;
import com.glamora_store.entity.ProductVariantValue;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.mapper.ProductVariantMapper;
import com.glamora_store.repository.AttributeValueRepository;
import com.glamora_store.repository.ProductRepository;
import com.glamora_store.repository.ProductVariantRepository;
import com.glamora_store.repository.ProductVariantValueRepository;
import com.glamora_store.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

  private final ProductVariantRepository productVariantRepository;
  private final ProductRepository productRepository;
  private final AttributeValueRepository attributeValueRepository;
  private final ProductVariantValueRepository productVariantValueRepository;
  private final ProductVariantMapper productVariantMapper;

  @Override
  @Transactional
  public ProductVariantAdminResponse createProductVariant(ProductVariantCreateRequest request) {
    Product product = productRepository.findById(request.getProductId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_NOT_FOUND.getMessage()));

    ProductVariant productVariant = productVariantMapper.toProductVariant(request);
    productVariant.setProduct(product);
    productVariant.setIsDeleted(false);

    // Auto-generate SKU if not provided
    if (request.getSku() == null || request.getSku().trim().isEmpty()) {
      String generatedSku = generateSku();
      productVariant.setSku(generatedSku);
    } else {
      // Check if SKU already exists when manually provided
      if (productVariantRepository.existsBySku(request.getSku())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            ErrorMessage.PRODUCT_VARIANT_SKU_EXISTS.getMessage());
      }
    }

    ProductVariant savedVariant = productVariantRepository.save(productVariant);

    // Handle attribute values
    if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
      for (Long attributeValueId : request.getAttributeValueIds()) {
        AttributeValue attributeValue = attributeValueRepository.findById(attributeValueId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ErrorMessage.ATTRIBUTE_VALUE_NOT_FOUND.getMessage()));

        ProductVariantValue pvv = new ProductVariantValue();
        pvv.setVariant(savedVariant);
        pvv.setAttributeValue(attributeValue);
        productVariantValueRepository.save(pvv);
      }
    }

    return productVariantMapper
        .toProductVariantAdminResponse(productVariantRepository.findById(savedVariant.getId()).get());
  }

  @Override
  @Transactional
  public ProductVariantAdminResponse updateProductVariant(Long id, ProductVariantUpdateRequest request) {
    ProductVariant productVariant = productVariantRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

    // Check SKU uniqueness if it's being changed
    if (request.getSku() != null && !request.getSku().equals(productVariant.getSku())) {
      if (productVariantRepository.existsBySku(request.getSku())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            ErrorMessage.PRODUCT_VARIANT_SKU_EXISTS.getMessage());
      }
    }

    productVariantMapper.updateProductVariantFromRequest(request, productVariant);

    // Update attribute values if provided
    if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
      // Delete existing variant values
      productVariantValueRepository.deleteByVariantId(id);

      // Flush to ensure deletion is completed before insert
      productVariantValueRepository.flush();

      // Add new variant values
      for (Long attributeValueId : request.getAttributeValueIds()) {
        AttributeValue attributeValue = attributeValueRepository.findById(attributeValueId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    ErrorMessage.ATTRIBUTE_VALUE_NOT_FOUND.getMessage()));

        ProductVariantValue pvv = new ProductVariantValue();
        pvv.setVariant(productVariant);
        pvv.setAttributeValue(attributeValue);
        productVariantValueRepository.save(pvv);
      }
    }

    ProductVariant updatedVariant = productVariantRepository.save(productVariant);
    return productVariantMapper
        .toProductVariantAdminResponse(productVariantRepository.findById(updatedVariant.getId()).get());
  }

  @Override
  @Transactional
  public void deleteProductVariant(Long id) {
    ProductVariant productVariant = productVariantRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

    productVariant.setIsDeleted(true);
    productVariantRepository.save(productVariant);
  }

  @Override
  @Transactional
  public ProductVariantAdminResponse activateProductVariant(Long id) {
    ProductVariant productVariant = productVariantRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

    productVariant.setIsDeleted(false);
    ProductVariant activatedVariant = productVariantRepository.save(productVariant);
    return productVariantMapper.toProductVariantAdminResponse(activatedVariant);
  }

  @Override
  public ProductVariantAdminResponse getProductVariantById(Long id) {
    ProductVariant productVariant = productVariantRepository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));
    return productVariantMapper.toProductVariantAdminResponse(productVariant);
  }

  @Override
  public PageResponse<ProductVariantAdminResponse> searchProductVariants(Long productId, String keyword,
      Boolean isDeleted, Pageable pageable) {
    Specification<ProductVariant> spec = (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (productId != null) {
        predicates.add(cb.equal(root.get("product").get("id"), productId));
      }

      if (keyword != null && !keyword.trim().isEmpty()) {
        String searchPattern = "%" + keyword.toLowerCase() + "%";
        predicates.add(cb.like(cb.lower(root.get("sku")), searchPattern));
      }

      if (isDeleted != null) {
        predicates.add(cb.equal(root.get("isDeleted"), isDeleted));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Page<ProductVariant> productVariants = productVariantRepository.findAll(spec, pageable);
    return PageResponse.from(productVariants.map(productVariantMapper::toProductVariantAdminResponse));
  }

  // Helper methods

  /**
   * Generate SKU code for product variant.
   * Format: GLM-{5-DIGIT-SEQUENCE}
   * Example: GLM-00001, GLM-00002, GLM-99999, GLM-100000
   * 
   * Benefits:
   * - GLM = GLaMora brand identifier (professional and branded)
   * - Global unique identifier across all products
   * - No dependency on product ID (works with MongoDB ID or any ID format)
   * - Thread-safe and multi-instance safe using PostgreSQL sequence
   * - Clean, short, and easy to remember
   * - Sequential for easy tracking and inventory management
   * - Friendly for customers and staff
   */
  private String generateSku() {
    // Get next sequence number from PostgreSQL sequence
    Long sequenceNumber = productVariantRepository.getNextSkuSequence();

    // Format as minimum 5-digit sequence (00001, 00002, ..., 99999, 100000...)
    String sequence = String.format("%05d", sequenceNumber);

    return "GLM-" + sequence;
  }
}
