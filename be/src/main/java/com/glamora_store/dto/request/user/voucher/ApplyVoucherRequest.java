package com.glamora_store.dto.request.user.voucher;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoucherRequest {

  @NotBlank(message = "VOUCHER_CODE_REQUIRED")
  private String voucherCode;
}
