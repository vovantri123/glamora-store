package com.glamora_store.dto.response.common.voucher;

import com.glamora_store.enums.DiscountType;
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
public class VoucherResponse {

  private Long id;
  private String code;
  private String name;
  private String description;
  private DiscountType discountType;
  private BigDecimal discountValue;
  private BigDecimal minOrderValue;
  private BigDecimal maxDiscountAmount;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer usageLimit;
  private Integer usagePerUser;
  private Integer usedCount;
  private Boolean isActive;
  private Boolean isValid;
}
