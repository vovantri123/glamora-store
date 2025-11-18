package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.product.ProductCreateRequest;
import com.glamora_store.dto.request.admin.product.ProductUpdateRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.admin.product.ProductAdminResponse;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

/**
 * Controller xử lý các API quản lý products cho admin
 * Admin có thể CRUD tất cả products trong hệ thống
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/products")
public class AdminProductController {
        private final ProductService productService;

        @PostMapping
        public ApiResponse<ProductAdminResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
                return new ApiResponse<>(
                                SuccessMessage.CREATE_PRODUCT_SUCCESS.getMessage(),
                                productService.createProduct(request));
        }

        @PutMapping("/{productId}")
        public ApiResponse<ProductAdminResponse> updateProduct(
                        @PathVariable Long productId,
                        @Valid @RequestBody ProductUpdateRequest request) {
                return new ApiResponse<>(
                                SuccessMessage.UPDATE_PRODUCT_SUCCESS.getMessage(),
                                productService.updateProduct(productId, request));
        }

        @DeleteMapping("/{productId}")
        public ApiResponse<String> deleteProduct(@PathVariable Long productId) {
                productService.deleteProduct(productId);
                return new ApiResponse<>(SuccessMessage.DELETE_PRODUCT_SUCCESS.getMessage());
        }

        @PutMapping("/{productId}/activate")
        public ApiResponse<ProductAdminResponse> activateProduct(@PathVariable Long productId) {
                return new ApiResponse<>(
                                SuccessMessage.ACTIVATE_PRODUCT_SUCCESS.getMessage(),
                                productService.activateProduct(productId));
        }

        @GetMapping
        public ApiResponse<PageResponse<ProductAdminResponse>> searchProducts(
                        @RequestParam(required = false) Long categoryId,
                        @RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "false") boolean isDeleted,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "desc") String sortDir) {

                Sort sort = sortDir.equalsIgnoreCase("asc")
                                ? Sort.by(sortBy).ascending()
                                : Sort.by(sortBy).descending();

                Pageable pageable = PageRequest.of(page, size, sort);
                PageResponse<ProductAdminResponse> result = productService.searchProductsForAdmin(categoryId, keyword,
                                isDeleted, pageable);

                String message = (result.getContent().isEmpty())
                                ? SuccessMessage.NO_DATA_FOUND.getMessage()
                                : SuccessMessage.SEARCH_PRODUCT_SUCCESS.getMessage();

                return new ApiResponse<>(message, result);
        }

        @GetMapping("/{productId}")
        public ApiResponse<ProductAdminResponse> getProductById(@PathVariable Long productId) {
                return new ApiResponse<>(
                                SuccessMessage.GET_PRODUCT_SUCCESS.getMessage(),
                                productService.getProductByIdForAdmin(productId));
        }
}
