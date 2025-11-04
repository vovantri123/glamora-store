package com.glamora_store.service;

import com.glamora_store.dto.request.admin.order.UpdateOrderStatusRequest;
import com.glamora_store.dto.request.user.order.CancelOrderRequest;
import com.glamora_store.dto.request.user.order.CreateOrderRequest;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.order.OrderResponse;
import com.glamora_store.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

public interface OrderService {

  // User endpoints
  OrderResponse createOrder(CreateOrderRequest request);

  OrderResponse getMyOrderById(Long orderId);

  OrderResponse getMyOrderByCode(String orderCode);

  PageResponse<OrderResponse> searchMyOrders(OrderStatus status, String orderCode, Pageable pageable);

  OrderResponse cancelMyOrder(Long orderId, CancelOrderRequest request);

  OrderResponse confirmOrderReceived(Long orderId);

  // Admin endpoints
  OrderResponse getOrderById(Long orderId);

  OrderResponse getOrderByCode(String orderCode);

  PageResponse<OrderResponse> searchOrders(
      OrderStatus status,
      Long userId,
      String orderCode,
      String userEmail,
      String userFullName,
      Pageable pageable);

  OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

  void deleteOrder(Long orderId);
}
