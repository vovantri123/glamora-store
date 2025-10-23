package com.glamora_store.dto.response.user.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherDiscountResponse {

  private String voucherCode;
  private BigDecimal discountAmount;
  private BigDecimal originalAmount;
  private BigDecimal finalAmount;
}
