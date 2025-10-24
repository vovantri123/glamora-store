package com.glamora_store.controller.user;

import com.glamora_store.dto.request.user.payment.CreatePaymentRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.common.payment.PaymentResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment APIs")
public class UserPaymentController {

  private final PaymentService paymentService;

  @PostMapping
  @Operation(summary = "Create payment for order")
  public ApiResponse<PaymentResponse> createPayment(
    @Valid @RequestBody CreatePaymentRequest request) {
    PaymentResponse response = paymentService.createPayment(request);
    return ApiResponse.<PaymentResponse>builder()
      .message(SuccessMessage.CREATE_PAYMENT_SUCCESS.getMessage())
      .data(response)
      .build();
  }

  @GetMapping("/order/{orderId}")
  @Operation(summary = "Get payment by order ID")
  public ApiResponse<PaymentResponse> getPaymentByOrderId(@PathVariable Long orderId) {
    PaymentResponse response = paymentService.getPaymentByOrderId(orderId);
    return ApiResponse.<PaymentResponse>builder()
      .message(SuccessMessage.GET_PAYMENT_SUCCESS.getMessage())
      .data(response)
      .build();
  }

  // Handle URL mà VNPay redirect user về sau khi thanh toán xong
  @GetMapping("/vnpay-return")
  @Operation(summary = "Handle VNPay return callback and redirect to frontend")
  public void handleVNPayReturn(
    @RequestParam Map<String, String> vnpayParams,
    HttpServletResponse response) throws IOException {

    PaymentResponse paymentResponse = paymentService.handleVNPayReturn(vnpayParams);

    // Redirect về frontend với kết quả thanh toán
    String frontendUrl = "http://localhost:3000/payment-result";
    String redirectUrl = frontendUrl + "?status=" + paymentResponse.getStatus()
      + "&orderId=" + paymentResponse.getOrderId()
      + "&amount=" + paymentResponse.getAmount()
      + "&orderCode=" + paymentResponse.getOrderCode();

    // Thêm failedReason nếu có lỗi
    if (paymentResponse.getFailedReason() != null) {
      redirectUrl += "&failedReason=" + java.net.URLEncoder.encode(paymentResponse.getFailedReason(), "UTF-8");
    }

    response.sendRedirect(redirectUrl);
  }

  @PutMapping("/cancel/order/{orderId}")
  @Operation(summary = "Cancel payment by order ID")
  public ApiResponse<PaymentResponse> cancelPayment(@PathVariable Long orderId) {
    PaymentResponse response = paymentService.cancelPayment(orderId);
    return ApiResponse.<PaymentResponse>builder()
      .message("Payment cancelled successfully")
      .data(response)
      .build();
  }

  @PutMapping("/check-expired/{paymentId}")
  @Operation(summary = "Check and update expired payment")
  public ApiResponse<PaymentResponse> checkExpiredPayment(@PathVariable Long paymentId) {
    PaymentResponse response = paymentService.checkAndUpdateExpiredPayment(paymentId);
    return ApiResponse.<PaymentResponse>builder()
      .message("Payment status checked successfully")
      .data(response)
      .build();
  }
}
