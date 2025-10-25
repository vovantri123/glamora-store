package com.glamora_store.enums;

public enum PaymentStatus {
  PENDING, // Chờ thanh toán
  SUCCESS, // Thanh toán thành công
  FAILED, // Thanh toán thất bại
  CANCELLED, // Đơn hàng bị hủy
  EXPIRED // Payment URL hết hạn (VNPay)
}
