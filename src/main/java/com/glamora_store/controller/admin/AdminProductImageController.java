package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.product_image.ProductImageCreateRequest;
import com.glamora_store.dto.request.admin.product_image.ProductImageUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.product_image.ProductImageAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product-images")
public class AdminProductImageController {

  private final ProductImageService productImageService;

  @PostMapping
  public ApiResponse<ProductImageAdminResponse> createProductImage(
    @Valid @RequestBody ProductImageCreateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.CREATE_PRODUCT_IMAGE_SUCCESS.getMessage(),
      productImageService.createProductImage(request));
  }

  @PutMapping("/{imageId}")
  public ApiResponse<ProductImageAdminResponse> updateProductImage(
    @PathVariable Long imageId,
    @Valid @RequestBody ProductImageUpdateRequest request) {
    return new ApiResponse<>(
      SuccessMessage.UPDATE_PRODUCT_IMAGE_SUCCESS.getMessage(),
      productImageService.updateProductImage(imageId, request));
  }

  @DeleteMapping("/{imageId}")
  public ApiResponse<String> deleteProductImage(@PathVariable Long imageId) {
    productImageService.deleteProductImage(imageId);
    return new ApiResponse<>(SuccessMessage.DELETE_PRODUCT_IMAGE_SUCCESS.getMessage());
  }

  @GetMapping("/{imageId}")
  public ApiResponse<ProductImageAdminResponse> getProductImage(@PathVariable Long imageId) {
    return new ApiResponse<>(
      SuccessMessage.GET_PRODUCT_IMAGE_SUCCESS.getMessage(),
      productImageService.getProductImageById(imageId));
  }

  @GetMapping
  public ApiResponse<PageResponse<ProductImageAdminResponse>> getAllProductImages(
    @RequestParam(required = false) Long productId,
    @RequestParam(required = false) Long variantId,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "displayOrder") String sortBy,
    @RequestParam(defaultValue = "asc") String sortDir) {

    // Nếu không filter theo productId hoặc variantId, sort theo id thay vì
    // displayOrder
    if (productId == null && variantId == null) {
      sortBy = "id";
    }

    Sort sort = sortDir.equalsIgnoreCase("asc")
      ? Sort.by(sortBy).ascending()
      : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(page, size, sort);
    PageResponse<ProductImageAdminResponse> result = productImageService.getAllProductImages(productId,
      variantId,
      pageable);

    String message = (result.getContent().isEmpty())
      ? SuccessMessage.NO_DATA_FOUND.getMessage()
      : SuccessMessage.GET_ALL_PRODUCT_IMAGE_SUCCESS.getMessage();

    return new ApiResponse<>(message, result);
  }
}
