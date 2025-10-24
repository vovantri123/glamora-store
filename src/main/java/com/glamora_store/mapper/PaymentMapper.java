package com.glamora_store.mapper;

import com.glamora_store.dto.response.common.payment.PaymentMethodResponse;
import com.glamora_store.dto.response.common.payment.PaymentResponse;
import com.glamora_store.entity.Payment;
import com.glamora_store.entity.PaymentMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

  @Mapping(source = "order.id", target = "orderId")
  @Mapping(source = "order.orderCode", target = "orderCode")
  PaymentResponse toPaymentResponse(Payment payment);

  PaymentMethodResponse toPaymentMethodResponse(PaymentMethod paymentMethod);
}
