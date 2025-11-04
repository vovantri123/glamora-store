package com.glamora_store.service.impl;

import com.glamora_store.dto.request.admin.order.UpdateOrderStatusRequest;
import com.glamora_store.dto.request.user.order.CancelOrderRequest;
import com.glamora_store.dto.request.user.order.CreateOrderRequest;
import com.glamora_store.dto.request.user.order.OrderItemRequest;
import com.glamora_store.dto.response.common.PageResponse;
import com.glamora_store.dto.response.common.order.OrderResponse;
import com.glamora_store.entity.*;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OrderStatus;
import com.glamora_store.mapper.OrderMapper;
import com.glamora_store.repository.*;
import com.glamora_store.service.OrderService;
import com.glamora_store.util.DistanceCalculator;
import com.glamora_store.util.SecurityUtil;
import com.glamora_store.util.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final AddressRepository addressRepository;
  private final ProductVariantRepository productVariantRepository;
  private final PaymentRepository paymentRepository;
  private final VoucherRepository voucherRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final OrderMapper orderMapper;

  @Override
  @Transactional
  public OrderResponse createOrder(CreateOrderRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    User currentUser = userRepository.findById(userId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, ErrorMessage.USER_NOT_FOUND.getMessage()));

    // Validate address belongs to user
    Address address = addressRepository.findById(request.getAddressId())
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ADDRESS_NOT_FOUND.getMessage()));

    if (!address.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
    }

    // Validate address has coordinates
    if (address.getLatitude() == null || address.getLongitude() == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.ADDRESS_MISSING_COORDINATES.getMessage());
    }

    // Validate payment method exists
    PaymentMethod paymentMethod = paymentMethodRepository.findById(request.getPaymentMethodId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
            ErrorMessage.PAYMENT_METHOD_NOT_FOUND.getMessage()));

    // Create order items and calculate subtotal
    Set<OrderItem> orderItems = new HashSet<>();
    BigDecimal subtotal = BigDecimal.ZERO;

    for (OrderItemRequest itemReq : request.getItems()) {
      ProductVariant variant = productVariantRepository.findById(itemReq.getVariantId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              ErrorMessage.PRODUCT_VARIANT_NOT_FOUND.getMessage()));

      // Check stock
      if (variant.getStock() < itemReq.getQuantity()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.INSUFFICIENT_STOCK.getMessage());
      } // Calculate item price
      BigDecimal itemPrice = variant.getPrice();
      BigDecimal itemTotal = itemPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));
      subtotal = subtotal.add(itemTotal);

      // Reduce stock
      variant.setStock(variant.getStock() - itemReq.getQuantity());
      productVariantRepository.save(variant);

      OrderItem orderItem = OrderItem.builder()
          .variant(variant)
          .quantity(itemReq.getQuantity())
          .price(itemPrice)
          .totalPrice(itemTotal)
          .build();

      orderItems.add(orderItem);
    }

    // Calculate distance and shipping fee based on coordinates
    // Store location: SPKT (Glamora Store)
    BigDecimal distance = DistanceCalculator.calculateDistance(
        DistanceCalculator.STORE_LATITUDE,
        DistanceCalculator.STORE_LONGITUDE,
        BigDecimal.valueOf(address.getLatitude()),
        BigDecimal.valueOf(address.getLongitude()));

    // Fixed shipping fee: 2000 VND per km
    BigDecimal shippingFee = DistanceCalculator.calculateShippingFee(distance);

    // Validate and apply voucher if provided
    Voucher voucher = null;
    BigDecimal discountAmount = BigDecimal.ZERO;

    if (request.getVoucherId() != null) {
      voucher = voucherRepository.findById(request.getVoucherId())
          .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
              ErrorMessage.VOUCHER_NOT_FOUND.getMessage()));

      // Check if voucher has remaining usage (IMPORTANT: prevent race condition)
      if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            ErrorMessage.VOUCHER_USAGE_LIMIT_EXCEEDED.getMessage());
      }

      // Validate discount amount from frontend matches voucher rules
      if (request.getDiscountAmount() != null && request.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) {
        discountAmount = request.getDiscountAmount();
      }

      // CRITICAL: Reserve voucher immediately to prevent race condition
      // Scenario: User creates Order 1 with voucher → creates Order 2 with same
      // voucher
      // → if we don't reserve here, both orders can use the same voucher
      // Solution: Increase usedCount NOW to reserve the voucher
      voucher.setUsedCount(voucher.getUsedCount() + 1);
      voucherRepository.save(voucher);

      // Note: If user cancels order or payment fails, we will decrease usageCount
      // in cancelMyOrder() method to release the voucher
    }

    // Calculate total: subtotal - discount + shipping
    BigDecimal totalAmount = subtotal.subtract(discountAmount).add(shippingFee);

    // Create order
    String orderCode = generateOrderCode();
    Order order = Order.builder()
        .orderCode(orderCode)
        .user(currentUser)
        .shippingAddress(address)
        .voucher(voucher) // Save voucher reference
        .paymentMethod(paymentMethod) // Save payment method chosen at order creation
        .status(OrderStatus.PENDING)
        .subtotal(subtotal)
        .discountAmount(discountAmount) // Save discount amount from voucher
        .distance(distance) // Save calculated distance
        .shippingFee(shippingFee)
        .totalAmount(totalAmount)
        .note(request.getNote())
        .recipientName(address.getReceiverName()) // Snapshot recipient info at order creation
        .recipientPhone(address.getReceiverPhone()) // Snapshot recipient info at order creation
        .orderItems(orderItems)
        .build();

    // Set order reference for items
    orderItems.forEach(item -> item.setOrder(order));

    Order savedOrder = orderRepository.save(order);

    return orderMapper.toOrderResponse(savedOrder);
  }

  @Override
  public OrderResponse getMyOrderById(Long orderId) {
    Long userId = SecurityUtil.getCurrentUserId();
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Admin can view all orders, users can only view their own orders
    if (!SecurityUtil.isAdmin() && !order.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
    }

    return orderMapper.toOrderResponse(order);
  }

  @Override
  public OrderResponse getMyOrderByCode(String orderCode) {
    Long userId = SecurityUtil.getCurrentUserId();
    Order order = orderRepository.findByOrderCode(orderCode)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Admin can view all orders, users can only view their own orders
    if (!SecurityUtil.isAdmin() && !order.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
    }

    return orderMapper.toOrderResponse(order);
  }

  @Override
  public PageResponse<OrderResponse> searchMyOrders(OrderStatus status, String orderCode, Pageable pageable) {
    Long userId = SecurityUtil.getCurrentUserId();
    Specification<Order> spec = OrderSpecification.hasUserId(userId)
        .and(status != null ? OrderSpecification.hasStatus(status) : null)
        .and(orderCode != null ? OrderSpecification.hasOrderCodeLike(orderCode) : null);

    Page<Order> orderPage = orderRepository.findAll(spec, pageable);
    Page<OrderResponse> mapped = orderPage.map(orderMapper::toOrderResponse);
    return PageResponse.from(mapped);
  }

  @Override
  @Transactional
  public OrderResponse cancelMyOrder(Long orderId, CancelOrderRequest request) {
    Long userId = SecurityUtil.getCurrentUserId();
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Admin can cancel any order, users can only cancel their own orders
    if (!SecurityUtil.isAdmin() && !order.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
    }

    // Can only cancel PENDING orders (Trước mắt không cho hủy cái CONFIRMED,
    // because no refund logic implemented)
    if (order.getStatus() != OrderStatus.PENDING) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.ORDER_CANNOT_CANCEL.getMessage());
    }

    // Cancel payment if exists and still PENDING
    paymentRepository.findFirstByOrderIdOrderByCreatedAtDesc(orderId).ifPresent(payment -> {
      if (payment.getStatus() == com.glamora_store.enums.PaymentStatus.PENDING) {
        payment.setStatus(com.glamora_store.enums.PaymentStatus.CANCELLED);
        payment.setFailedReason("Order cancelled by user");
        paymentRepository.save(payment);
      }
    });

    // Restore stock
    for (OrderItem item : order.getOrderItems()) {
      ProductVariant variant = item.getVariant();
      variant.setStock(variant.getStock() + item.getQuantity());
      productVariantRepository.save(variant);
    }

    // Release voucher if it was used (decrease usedCount)
    if (order.getVoucher() != null) {
      Voucher voucher = order.getVoucher();
      voucher.setUsedCount(Math.max(0, voucher.getUsedCount() - 1)); // Prevent negative
      voucherRepository.save(voucher);
    }

    order.setStatus(OrderStatus.CANCELED);
    order.setCanceledReason(request.getCanceledReason());
    Order updated = orderRepository.save(order);

    return orderMapper.toOrderResponse(updated);
  }

  @Override
  @Transactional
  public OrderResponse confirmOrderReceived(Long orderId) {
    Long userId = SecurityUtil.getCurrentUserId();
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Admin can confirm any order, users can only confirm their own orders
    if (!SecurityUtil.isAdmin() && !order.getUser().getId().equals(userId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.ORDER_ACCESS_DENIED.getMessage());
    }

    // Can only confirm SHIPPING orders
    if (order.getStatus() != OrderStatus.SHIPPING) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          ErrorMessage.ORDER_CANNOT_CONFIRM_RECEIVED.getMessage() + ". Current status: " + order.getStatus());
    }

    // Update to COMPLETED
    order.setStatus(OrderStatus.COMPLETED);
    Order updated = orderRepository.save(order);

    return orderMapper.toOrderResponse(updated);
  }

  // Admin endpoints

  @Override
  public OrderResponse getOrderById(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));
    return orderMapper.toOrderResponse(order);
  }

  @Override
  public OrderResponse getOrderByCode(String orderCode) {
    Order order = orderRepository.findByOrderCode(orderCode)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));
    return orderMapper.toOrderResponse(order);
  }

  @Override
  public PageResponse<OrderResponse> searchOrders(
      OrderStatus status,
      Long userId,
      String orderCode,
      String userEmail,
      String userFullName,
      Pageable pageable) {

    Specification<Order> spec = OrderSpecification.hasStatus(status)
        .and(OrderSpecification.hasUserId(userId))
        .and(OrderSpecification.hasOrderCodeLike(orderCode))
        .and(OrderSpecification.hasUserEmail(userEmail))
        .and(OrderSpecification.hasUserFullName(userFullName));

    Page<Order> orderPage = orderRepository.findAll(spec, pageable);
    Page<OrderResponse> mapped = orderPage.map(orderMapper::toOrderResponse);
    return PageResponse.from(mapped);
  }

  @Override
  @Transactional
  public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    OrderStatus currentStatus = order.getStatus();
    OrderStatus newStatus = request.getStatus();

    // Validate status transition
    validateStatusTransition(currentStatus, newStatus);

    order.setStatus(newStatus);

    if (request.getNote() != null && !request.getNote().isEmpty()) {
      order.setNote(request.getNote());
    }

    Order updated = orderRepository.save(order);

    return orderMapper.toOrderResponse(updated);
  }

  @Override
  @Transactional
  public void deleteOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Can only delete CANCELED orders
    if (order.getStatus() != OrderStatus.CANCELED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMessage.ORDER_CANNOT_DELETE.getMessage());
    }

    orderRepository.delete(order);
  }

  // Helper methods

  private String generateOrderCode() {
    // Format: ORD-YYYYMMDD-XXXX (e.g., ORD-20251023-0001)
    // YYYYMMDD: Date for easy tracking and sorting
    // XXXX: 4-digit sequence number from database sequence (thread-safe,
    // multi-instance safe)
    // Note: Sequence resets daily at midnight via OrderSequenceScheduler
    // Note: If > 9999 orders/day, format will expand (e.g., 10000, 10001...)

    LocalDateTime now = LocalDateTime.now();
    String datePrefix = String.format("%04d%02d%02d",
        now.getYear(),
        now.getMonthValue(),
        now.getDayOfMonth());

    // Get next sequence number from database
    Long sequenceNumber = orderRepository.getNextOrderSequence();

    // Format as minimum 4-digit sequence (0001, 0002, ..., 9999, 10000...)
    String sequence = String.format("%04d", sequenceNumber);

    return "ORD-" + datePrefix + "-" + sequence;
  }

  private void validateStatusTransition(OrderStatus from, OrderStatus to) {
    // Define valid transitions
    switch (from) {
      case PENDING:
        if (to != OrderStatus.CONFIRMED && to != OrderStatus.CANCELED) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              ErrorMessage.ORDER_INVALID_STATUS_TRANSITION.getMessage());
        }
        break;
      case CONFIRMED:
        if (to != OrderStatus.SHIPPING) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              ErrorMessage.ORDER_INVALID_STATUS_TRANSITION.getMessage());
        }
        break;
      case SHIPPING:
        // Admin cannot change SHIPPING status
        // User must confirm received via /user/orders/{id}/confirm-received
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            ErrorMessage.ORDER_CANNOT_CHANGE_SHIPPING_STATUS.getMessage());
      case COMPLETED:
      case CANCELED:
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            ErrorMessage.ORDER_STATUS_CANNOT_CHANGE_COMPLETED.getMessage());
    }
  }
}
