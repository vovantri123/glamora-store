package com.glamora_store.controller.common;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.product.ProductResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/products")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping
  public ApiResponse<PageResponse<ProductResponse>> searchProducts(
    @RequestParam(required = false) Long categoryId,
    @RequestParam(required = false) String keyword,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "12") int size,
    @RequestParam(defaultValue = "id") String sortBy,
    @RequestParam(defaultValue = "desc") String sortDir) {

    PageResponse<ProductResponse> products;

    // Sử dụng method searchProducts có sẵn với logic routing
    products = productService.searchProducts(categoryId, keyword, page, size, sortBy, sortDir);

    String message = products.getContent().isEmpty()
      ? SuccessMessage.NO_DATA_FOUND.getMessage()
      : SuccessMessage.GET_ALL_PRODUCT_SUCCESS.getMessage();
    return new ApiResponse<>(message, products);
  }

  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {
    return new ApiResponse<>(
      SuccessMessage.GET_PRODUCT_SUCCESS.getMessage(),
      productService.getProductById(id));
  }
}
