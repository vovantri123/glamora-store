package com.glamora_store.service;

import com.glamora_store.dto.request.user.payment.CreatePaymentRequest;
import com.glamora_store.dto.response.common.payment.PaymentMethodResponse;
import com.glamora_store.dto.response.common.payment.PaymentResponse;
import java.util.List;
import java.util.Map;

public interface PaymentService {

  PaymentResponse createPayment(CreatePaymentRequest request);

  PaymentResponse getPaymentByOrderId(Long orderId);

  PaymentResponse handleVNPayReturn(Map<String, String> vnpayParams);

  PaymentResponse cancelPayment(Long orderId);

  PaymentResponse checkAndUpdateExpiredPayment(Long paymentId);

  List<PaymentMethodResponse> getAllPaymentMethods();

  PaymentMethodResponse getPaymentMethodById(Long id);
}
