package com.glamora_store.enums;

public enum OrderStatus {
  PENDING, // Chờ xử lý
  CONFIRMED, // Đã xác nhận (thay cho PAID)
  SHIPPING, // Đang giao
  COMPLETED, // Hoàn tất
  CANCELED // Đã hủy
}
