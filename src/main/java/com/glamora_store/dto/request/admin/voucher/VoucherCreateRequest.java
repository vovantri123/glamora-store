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
public class VoucherCreateRequest {

  @NotBlank(message = "VOUCHER_CODE_REQUIRED")
  @Size(max = 50, message = "VOUCHER_CODE_TOO_LONG")
  private String code;

  @NotBlank(message = "VOUCHER_NAME_REQUIRED")
  @Size(max = 255, message = "VOUCHER_NAME_TOO_LONG")
  private String name;

  private String description;

  @NotNull(message = "VOUCHER_DISCOUNT_TYPE_REQUIRED")
  private DiscountType discountType;

  @NotNull(message = "VOUCHER_DISCOUNT_VALUE_REQUIRED")
  @DecimalMin(value = "0.0", inclusive = false, message = "VOUCHER_DISCOUNT_VALUE_INVALID")
  private BigDecimal discountValue;

  @DecimalMin(value = "0.0", message = "VOUCHER_MIN_ORDER_VALUE_INVALID")
  private BigDecimal minOrderValue;

  @DecimalMin(value = "0.0", message = "VOUCHER_MAX_DISCOUNT_INVALID")
  private BigDecimal maxDiscountAmount;

  @NotNull(message = "VOUCHER_START_DATE_REQUIRED")
  @Future(message = "VOUCHER_START_DATE_FUTURE")
  private LocalDateTime startDate;

  @NotNull(message = "VOUCHER_END_DATE_REQUIRED")
  @Future(message = "VOUCHER_END_DATE_FUTURE")
  private LocalDateTime endDate;

  @Min(value = 1, message = "VOUCHER_USAGE_LIMIT_INVALID")
  private Integer usageLimit;

  @Min(value = 1, message = "VOUCHER_USAGE_PER_USER_INVALID")
  private Integer usagePerUser;

  private Boolean isActive;
}
