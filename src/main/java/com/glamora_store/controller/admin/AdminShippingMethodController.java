package com.glamora_store.controller.admin;

import com.glamora_store.dto.request.admin.shipping.CreateShippingMethodRequest;
import com.glamora_store.dto.request.admin.shipping.UpdateShippingMethodRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.shipping.ShippingMethodResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ShippingMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/shipping-methods")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Shipping Methods - Admin", description = "Admin endpoints for managing shipping methods")
public class AdminShippingMethodController {

    private final ShippingMethodService shippingMethodService;

    @GetMapping
    @Operation(summary = "Get all shipping methods", description = "Get list of all shipping methods (including inactive ones)")
    public ApiResponse<List<ShippingMethodResponse>> getAllShippingMethods() {
        return new ApiResponse<>(
                SuccessMessage.OPERATION_SUCCESSFUL.getMessage(),
                shippingMethodService.getAllShippingMethods());
    }

    @PostMapping
    @Operation(summary = "Create new shipping method", description = "Create a new shipping method")
    public ApiResponse<ShippingMethodResponse> createShippingMethod(
            @Valid @RequestBody CreateShippingMethodRequest request) {
        return new ApiResponse<>(
                SuccessMessage.CREATED_SUCCESSFULLY.getMessage(),
                shippingMethodService.createShippingMethod(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update shipping method", description = "Update an existing shipping method")
    public ApiResponse<ShippingMethodResponse> updateShippingMethod(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShippingMethodRequest request) {
        return new ApiResponse<>(
                SuccessMessage.UPDATED_SUCCESSFULLY.getMessage(),
                shippingMethodService.updateShippingMethod(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete shipping method", description = "Delete a shipping method permanently")
    public ApiResponse<Void> deleteShippingMethod(@PathVariable Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return new ApiResponse<>(SuccessMessage.DELETED_SUCCESSFULLY.getMessage());
    }

    @PatchMapping("/{id}/toggle-active")
    @Operation(summary = "Toggle active status", description = "Toggle active/inactive status of a shipping method")
    public ApiResponse<ShippingMethodResponse> toggleActiveStatus(@PathVariable Long id) {
        return new ApiResponse<>(
                SuccessMessage.UPDATED_SUCCESSFULLY.getMessage(),
                shippingMethodService.toggleActiveStatus(id));
    }
}
