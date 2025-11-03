package com.glamora_store.service.impl;

import com.glamora_store.config.VNPayConfig;
import com.glamora_store.dto.request.user.payment.CreatePaymentRequest;
import com.glamora_store.dto.response.common.payment.PaymentMethodResponse;
import com.glamora_store.dto.response.common.payment.PaymentResponse;
import com.glamora_store.entity.Order;
import com.glamora_store.entity.Payment;
import com.glamora_store.entity.PaymentMethod;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OrderStatus;
import com.glamora_store.enums.PaymentStatus;
import com.glamora_store.mapper.PaymentMapper;
import com.glamora_store.repository.OrderRepository;
import com.glamora_store.repository.PaymentMethodRepository;
import com.glamora_store.repository.PaymentRepository;
import com.glamora_store.service.CartService;
import com.glamora_store.service.PaymentService;
import com.glamora_store.util.VNPayUtil;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final PaymentMethodRepository paymentMethodRepository;
  private final OrderRepository orderRepository;
  private final CartService cartService;
  private final PaymentMapper paymentMapper;
  private final VNPayConfig vnPayConfig;

  @Override
  @Transactional
  public PaymentResponse createPayment(CreatePaymentRequest request) {
    // Kiểm tra order có tồn tại không
    Order order = orderRepository
        .findById(request.getOrderId())
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND.getMessage()));

    // Kiểm tra payment đã SUCCESS cho order này chưa
    if (paymentRepository.existsByOrderIdAndStatus(
        request.getOrderId(), PaymentStatus.SUCCESS)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ErrorMessage.PAYMENT_ALREADY_COMPLETED.getMessage());
    }

    // Hủy tất cả payment PENDING cũ trước khi tạo mới
    // (User có thể tạo payment VNPay nhiều lần nếu không thanh toán)
    paymentRepository.findByOrderIdOrderByCreatedAtDesc(request.getOrderId())
        .stream()
        .filter(p -> p.getStatus() == PaymentStatus.PENDING)
        .forEach(p -> {
          p.setStatus(PaymentStatus.CANCELLED);
          p.setFailedReason("Cancelled due to new payment creation");
          paymentRepository.save(p);
        });

    // Kiểm tra payment method có tồn tại và active không
    PaymentMethod paymentMethod = paymentMethodRepository
        .findById(request.getPaymentMethodId())
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_METHOD_NOT_FOUND.getMessage()));

    if (!paymentMethod.getIsActive()) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ErrorMessage.PAYMENT_METHOD_INACTIVE.getMessage());
    }

    // Tạo payment
    Payment payment = Payment.builder()
        .order(order)
        .paymentMethod(paymentMethod)
        .amount(order.getTotalAmount())
        .status(PaymentStatus.PENDING)
        .build();

    // Nếu là VNPay, tạo URL thanh toán
    if (paymentMethod.getName().contains("VNPay")) {
      String payUrl = createVNPayPaymentUrl(order);
      payment.setPayUrl(payUrl);
    }
    // Nếu là COD, set status = SUCCESS ngay và xử lý order + cart
    else if (paymentMethod.getName().contains("COD")) {
      payment.setStatus(PaymentStatus.SUCCESS);
      payment.setPaymentDate(LocalDateTime.now());

      // Cập nhật order status thành PAID
      order.setStatus(OrderStatus.PAID);
      orderRepository.save(order);

      // Voucher will be applied separately via Payment API, not at order creation
      // No need to increment voucher usage here

      // Xóa các cart items đã mua (theo variant IDs trong order)
      List<Long> variantIds = order.getOrderItems().stream()
          .map(item -> item.getVariant().getId())
          .toList();
      cartService.removeCartItemsByVariantIds(order.getUser().getId(), variantIds);
    }

    payment = paymentRepository.save(payment);

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  public PaymentResponse getPaymentByOrderId(Long orderId) {
    // Lấy payment mới nhất (theo createdAt desc)
    Payment payment = paymentRepository
        .findFirstByOrderIdOrderByCreatedAtDesc(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  @Transactional
  public PaymentResponse handleVNPayReturn(Map<String, String> vnpayParams) {
    String vnpSecureHash = vnpayParams.get("vnp_SecureHash");
    vnpayParams.remove("vnp_SecureHashType");
    vnpayParams.remove("vnp_SecureHash");

    String signValue = VNPayUtil.hashAllFields(vnpayParams, vnPayConfig.getHashSecret());

    if (!signValue.equals(vnpSecureHash)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, ErrorMessage.VNPAY_SIGNATURE_INVALID.getMessage());
    }

    String transactionId = vnpayParams.get("vnp_TxnRef");
    String responseCode = vnpayParams.get("vnp_ResponseCode");

    Payment payment = paymentRepository
        .findByTransactionId(transactionId)
        .orElseGet(
            () -> {
              // Nếu không tìm thấy payment theo transactionId, tìm theo orderId (payment mới
              // nhất)
              String orderInfo = vnpayParams.get("vnp_OrderInfo");
              if (orderInfo != null && orderInfo.startsWith("Payment for order ")) {
                String orderCode = orderInfo.replace("Payment for order ", "");
                Order order = orderRepository
                    .findByOrderCode(orderCode)
                    .orElseThrow(
                        () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            ErrorMessage.ORDER_NOT_FOUND.getMessage()));
                // return trong lambda chỉ thoát khỏi lambda và trả về giá trị cho orElseGet(),
                // method vẫn tiếp tục chạy!
                return paymentRepository
                    .findFirstByOrderIdOrderByCreatedAtDesc(order.getId())
                    .orElseThrow(
                        () -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            ErrorMessage.PAYMENT_NOT_FOUND.getMessage()));
              }
              throw new ResponseStatusException(
                  HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_NOT_FOUND.getMessage());
            });

    // Cập nhật thông tin payment
    payment.setTransactionId(transactionId);

    // Xử lý response code từ VNPay
    if ("00".equals(responseCode)) {
      // Thanh toán thành công
      payment.setStatus(PaymentStatus.SUCCESS);
      payment.setPaymentDate(LocalDateTime.now());

      // Cập nhật trạng thái Order thành PAID
      Order order = payment.getOrder();
      order.setStatus(OrderStatus.PAID);
      orderRepository.save(order);

      // Voucher will be applied separately via Payment API, not at order creation
      // No need to increment voucher usage here

      // Xóa các cart items đã mua (theo variant IDs trong order)
      List<Long> variantIds = order.getOrderItems().stream()
          .map(item -> item.getVariant().getId())
          .toList();
      cartService.removeCartItemsByVariantIds(order.getUser().getId(), variantIds);
    } else if ("24".equals(responseCode)) {
      // Giao dịch bị hủy bởi user
      payment.setStatus(PaymentStatus.CANCELLED);
      payment.setFailedReason("Transaction cancelled by user");
    } else if ("11".equals(responseCode) || "12".equals(responseCode)) {
      // 11: Đã hết hạn chờ thanh toán.
      // 12: Thẻ/Tài khoản bị khóa
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Card/Account error - VNPay response code: " + responseCode);
    } else if ("51".equals(responseCode)) {
      // Tài khoản không đủ số dư
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Insufficient balance");
    } else if ("65".equals(responseCode)) {
      // Tài khoản đã vượt quá giới hạn giao dịch trong ngày
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Daily transaction limit exceeded");
    } else if ("75".equals(responseCode)) {
      // Ngân hàng thanh toán đang bảo trì
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Payment gateway is under maintenance");
    } else if ("79".equals(responseCode)) {
      // Giao dịch bị ngân hàng từ chối
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Transaction declined by bank");
    } else {
      // Các mã lỗi khác
      payment.setStatus(PaymentStatus.FAILED);
      payment.setFailedReason("Payment failed - VNPay response code: " + responseCode);
    }

    payment = paymentRepository.save(payment);

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  @Transactional
  public PaymentResponse cancelPayment(Long orderId) {
    // Lấy payment mới nhất (theo createdAt desc)
    Payment payment = paymentRepository
        .findFirstByOrderIdOrderByCreatedAtDesc(orderId)
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

    // Chỉ cho phép cancel payment ở trạng thái PENDING
    if (payment.getStatus() != PaymentStatus.PENDING) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Can only cancel payment in PENDING status. Current status: " + payment.getStatus());
    }

    payment.setStatus(PaymentStatus.CANCELLED);
    payment.setFailedReason("Payment cancelled by user or system");
    payment = paymentRepository.save(payment);

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  @Transactional
  public PaymentResponse checkAndUpdateExpiredPayment(Long paymentId) {
    Payment payment = paymentRepository
        .findById(paymentId)
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_NOT_FOUND.getMessage()));

    // Chỉ check expired cho payment PENDING và có payUrl (VNPay)
    if (payment.getStatus() != PaymentStatus.PENDING || payment.getPayUrl() == null) {
      return paymentMapper.toPaymentResponse(payment);
    }

    // VNPay payment URL hết hạn sau 15 phút
    LocalDateTime createdAt = payment.getCreatedAt();
    LocalDateTime now = LocalDateTime.now();
    long minutesDiff = java.time.Duration.between(createdAt, now).toMinutes();

    if (minutesDiff > 15) {
      payment.setStatus(PaymentStatus.EXPIRED);
      payment.setFailedReason("Payment URL expired after 15 minutes");
      payment = paymentRepository.save(payment);
    }

    return paymentMapper.toPaymentResponse(payment);
  }

  @Override
  public List<PaymentMethodResponse> getAllPaymentMethods() {
    return paymentMethodRepository.findByIsActiveTrue().stream()
        .map(paymentMapper::toPaymentMethodResponse)
        .toList();
  }

  @Override
  public PaymentMethodResponse getPaymentMethodById(Long id) {
    PaymentMethod paymentMethod = paymentMethodRepository
        .findById(id)
        .orElseThrow(
            () -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, ErrorMessage.PAYMENT_METHOD_NOT_FOUND.getMessage()));

    return paymentMapper.toPaymentMethodResponse(paymentMethod);
  }

  // Tạo VNPay payment URL với returnUrl cố định từ config
  private String createVNPayPaymentUrl(Order order) {
    try {
      // Kiểm tra config VNPay
      if (vnPayConfig.getTmnCode() == null || vnPayConfig.getTmnCode().isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.VNPAY_CONFIG_MISSING.getMessage());
      }

      Map<String, String> vnpParams = new HashMap<>();
      vnpParams.put("vnp_Version", vnPayConfig.getVersion());
      vnpParams.put("vnp_Command", vnPayConfig.getCommand());
      vnpParams.put("vnp_TmnCode", vnPayConfig.getTmnCode());

      // Số tiền (VNPay yêu cầu đơn vị VND, nhân 100)
      long amount = order.getTotalAmount().multiply(new BigDecimal(100)).longValue();
      vnpParams.put("vnp_Amount", String.valueOf(amount));

      vnpParams.put("vnp_CurrCode", "VND");
      vnpParams.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
      vnpParams.put("vnp_OrderInfo", "Payment for order " + order.getOrderCode());
      vnpParams.put("vnp_OrderType", vnPayConfig.getOrderType());

      String locate = "vn";
      vnpParams.put("vnp_Locale", locate);

      // Return URL luôn lấy từ config, không cho phép client tùy chỉnh
      vnpParams.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());

      String vnpIpAddr = "127.0.0.1";
      vnpParams.put("vnp_IpAddr", vnpIpAddr);

      Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
      SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
      String vnpCreateDate = formatter.format(cld.getTime());
      vnpParams.put("vnp_CreateDate", vnpCreateDate);

      cld.add(Calendar.MINUTE, 15);
      String vnpExpireDate = formatter.format(cld.getTime());
      vnpParams.put("vnp_ExpireDate", vnpExpireDate);

      // Tạo secure hash
      String hashData = VNPayUtil.hashAllFields(vnpParams, vnPayConfig.getHashSecret());
      vnpParams.put("vnp_SecureHash", hashData);

      // Tạo payment URL
      return VNPayUtil.getPaymentURL(vnpParams, vnPayConfig.getVnpayUrl());

    } catch (ResponseStatusException e) {
      throw e;
    } catch (Exception e) {
      log.error("Error creating VNPay payment URL", e);
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, ErrorMessage.VNPAY_PAYMENT_URL_ERROR.getMessage());
    }
  }

}
