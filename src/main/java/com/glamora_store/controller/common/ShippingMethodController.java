package com.glamora_store.controller.common;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.shipping.ShippingMethodResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.ShippingMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public/shipping-methods")
@RequiredArgsConstructor
@Tag(name = "Shipping Methods - Public", description = "Public endpoints for shipping methods")
public class ShippingMethodController {

  private final ShippingMethodService shippingMethodService;

  @GetMapping
  @Operation(summary = "Get all active shipping methods", description = "Get list of all active shipping methods available for customers")
  public ApiResponse<List<ShippingMethodResponse>> getAllActiveShippingMethods() {
    return new ApiResponse<>(
        SuccessMessage.OPERATION_SUCCESSFUL.getMessage(),
        shippingMethodService.getAllActiveShippingMethods()
    );
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get shipping method by ID", description = "Get detailed information of a specific shipping method")
  public ApiResponse<ShippingMethodResponse> getShippingMethodById(@PathVariable Long id) {
    return new ApiResponse<>(
        SuccessMessage.OPERATION_SUCCESSFUL.getMessage(),
        shippingMethodService.getShippingMethodById(id)
    );
  }
}
