package com.glamora_store.dto.request.admin.voucher;

import com.glamora_store.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherUpdateRequest {

  @Size(max = 255, message = "VOUCHER_NAME_TOO_LONG")
  private String name;

  private String description;

  private DiscountType discountType;

  @DecimalMin(value = "0.0", inclusive = false, message = "VOUCHER_DISCOUNT_VALUE_INVALID")
  private BigDecimal discountValue;

  @DecimalMin(value = "0.0", message = "VOUCHER_MIN_ORDER_VALUE_INVALID")
  private BigDecimal minOrderValue;

  @DecimalMin(value = "0.0", message = "VOUCHER_MAX_DISCOUNT_INVALID")
  private BigDecimal maxDiscountAmount;

  private LocalDateTime startDate;

  private LocalDateTime endDate;

  @Min(value = 1, message = "VOUCHER_USAGE_LIMIT_INVALID")
  private Integer usageLimit;

  @Min(value = 1, message = "VOUCHER_USAGE_PER_USER_INVALID")
  private Integer usagePerUser;

  private Boolean isActive;
}
