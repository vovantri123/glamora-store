package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.product_variant.ProductVariantCreateRequest;
import com.glamora_store.dto.request.admin.product_variant.ProductVariantUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.product_variant.ProductVariantAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product-variants")
public class AdminProductVariantController {

  private final ProductVariantService productVariantService;

  @PostMapping
  public ApiResponse<ProductVariantAdminResponse> createProductVariant(
      @Valid @RequestBody ProductVariantCreateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.CREATE_PRODUCT_VARIANT_SUCCESS.getMessage(),
        productVariantService.createProductVariant(request));
  }

  @PutMapping("/{variantId}")
  public ApiResponse<ProductVariantAdminResponse> updateProductVariant(
      @PathVariable Long variantId,
      @Valid @RequestBody ProductVariantUpdateRequest request) {
    return new ApiResponse<>(
        SuccessMessage.UPDATE_PRODUCT_VARIANT_SUCCESS.getMessage(),
        productVariantService.updateProductVariant(variantId, request));
  }

  @DeleteMapping("/{variantId}")
  public ApiResponse<String> deleteProductVariant(@PathVariable Long variantId) {
    productVariantService.deleteProductVariant(variantId);
    return new ApiResponse<>(SuccessMessage.DELETE_PRODUCT_VARIANT_SUCCESS.getMessage());
  }

  @PutMapping("/{variantId}/activate")
  public ApiResponse<ProductVariantAdminResponse> activateProductVariant(@PathVariable Long variantId) {
    return new ApiResponse<>(
        SuccessMessage.ACTIVATE_PRODUCT_VARIANT_SUCCESS.getMessage(),
        productVariantService.activateProductVariant(variantId));
  }

  @GetMapping("/{variantId}")
  public ApiResponse<ProductVariantAdminResponse> getProductVariant(@PathVariable Long variantId) {
    return new ApiResponse<>(
        SuccessMessage.GET_PRODUCT_VARIANT_SUCCESS.getMessage(),
        productVariantService.getProductVariantById(variantId));
  }

  @GetMapping
  public ApiResponse<PageResponse<ProductVariantAdminResponse>> searchProductVariants(
      @RequestParam(required = false) Long productId,
      @RequestParam(required = false) String keyword,
      @RequestParam(defaultValue = "false") boolean isDeleted,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "createdAt") String sortBy,
      @RequestParam(defaultValue = "desc") String sortDir) {

    Sort sort = sortDir.equalsIgnoreCase("asc")
        ? Sort.by(sortBy).ascending()
        : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    PageResponse<ProductVariantAdminResponse> result = productVariantService.searchProductVariants(productId,
        keyword, isDeleted, pageable);

    String message = (result.getContent().isEmpty())
        ? SuccessMessage.NO_DATA_FOUND.getMessage()
        : SuccessMessage.GET_ALL_PRODUCT_VARIANT_SUCCESS.getMessage();

    return new ApiResponse<>(message, result);
  }
}
