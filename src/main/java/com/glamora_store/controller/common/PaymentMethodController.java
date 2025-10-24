package com.glamora_store.controller.common;

import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.payment.PaymentMethodResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/payment-methods")
@RequiredArgsConstructor
@Tag(name = "Payment Method", description = "Payment Method APIs")
public class PaymentMethodController {

  private final PaymentService paymentService;

  @GetMapping
  @Operation(summary = "Get all active payment methods")
  public ApiResponse<List<PaymentMethodResponse>> getAllPaymentMethods() {
    List<PaymentMethodResponse> response = paymentService.getAllPaymentMethods();
    return ApiResponse.<List<PaymentMethodResponse>>builder()
        .message(SuccessMessage.GET_ALL_PAYMENT_METHOD_SUCCESS.getMessage())
        .data(response)
        .build();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get payment method by ID")
  public ApiResponse<PaymentMethodResponse> getPaymentMethodById(@PathVariable Long id) {
    PaymentMethodResponse response = paymentService.getPaymentMethodById(id);
    return ApiResponse.<PaymentMethodResponse>builder()
        .message(SuccessMessage.GET_PAYMENT_METHOD_SUCCESS.getMessage())
        .data(response)
        .build();
  }
}
