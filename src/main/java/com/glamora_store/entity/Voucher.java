package com.glamora_store.entity;

import com.glamora_store.enums.DiscountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "vouchers")
public class Voucher extends AuditableEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false, unique = true, length = 50)
  private String code; // Mã voucher (ví dụ: GLAMORA2025)

  @Column(name = "name", nullable = false, length = 255)
  private String name; // Tên voucher

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  // Soft delete field
  @Column(name = "is_deleted", nullable = false)
  @Builder.Default
  private Boolean isDeleted = false;

  // Loại giảm giá: PERCENTAGE hoặc FIXED_AMOUNT
  @Enumerated(EnumType.STRING)
  @Column(name = "discount_type", nullable = false, length = 20)
  private DiscountType discountType;

  @Column(name = "discount_value", columnDefinition = "DECIMAL(10,2)", nullable = false)
  private BigDecimal discountValue; // 10% hoặc 50,000 VNĐ tùy loại

  @Column(name = "min_order_value", columnDefinition = "DECIMAL(10,2)")
  private BigDecimal minOrderValue; // Giá trị đơn hàng tối thiểu để được áp dụng

  @Column(name = "max_discount_amount", columnDefinition = "DECIMAL(10,2)")
  private BigDecimal maxDiscountAmount; // Giảm tối đa bao nhiêu (nếu là %)

  @Column(name = "start_date", nullable = false)
  private LocalDateTime startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDateTime endDate;

  @Column(name = "usage_limit")
  private Integer usageLimit; // Tổng số lần sử dụng tối đa (toàn hệ thống)

  @Column(name = "usage_per_user")
  @Builder.Default
  private Integer usagePerUser = 1; // Mỗi user dùng được bao nhiêu lần

  @Column(name = "used_count")
  @Builder.Default
  private Integer usedCount = 0; // Đếm số lần đã dùng

  @Column(name = "is_active")
  @Builder.Default
  private Boolean isActive = true; // Bật/tắt voucher (khác với soft delete)

  @OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL)
  @Builder.Default
  private Set<UserVoucher> userVouchers = new HashSet<>();

  @OneToMany(mappedBy = "voucher")
  @Builder.Default
  private Set<Order> orders = new HashSet<>();

  // Kiểm tra voucher còn hiệu lực không
  public boolean isValid() {
    LocalDateTime now = LocalDateTime.now();
    return Boolean.TRUE.equals(isActive) &&
        now.isAfter(startDate) &&
        now.isBefore(endDate) &&
        (usageLimit == null || usedCount < usageLimit);
  }

  // Tính số tiền giảm
  public BigDecimal calculateDiscount(BigDecimal orderAmount) {
    if (minOrderValue != null && orderAmount.compareTo(minOrderValue) < 0) {
      return BigDecimal.ZERO;
    }

    BigDecimal discount = BigDecimal.ZERO;

    if (discountType == DiscountType.FIXED_AMOUNT) {
      discount = discountValue;
    } else if (discountType == DiscountType.PERCENTAGE) {
      discount = orderAmount.multiply(discountValue).divide(new BigDecimal("100"));
      if (maxDiscountAmount != null && discount.compareTo(maxDiscountAmount) > 0) {
        discount = maxDiscountAmount;
      }
    }

    return discount.min(orderAmount); // Không được giảm quá tổng đơn hàng
  }
}
