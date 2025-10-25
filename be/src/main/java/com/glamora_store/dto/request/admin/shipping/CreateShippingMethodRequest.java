package com.glamora_store.dto.request.admin.shipping;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShippingMethodRequest {

  @NotBlank(message = "SHIPPING_METHOD_NAME_REQUIRED")
  @Size(max = 100, message = "SHIPPING_METHOD_NAME_TOO_LONG")
  private String name;

  @NotBlank(message = "SHIPPING_METHOD_CODE_REQUIRED")
  @Size(max = 50, message = "SHIPPING_METHOD_CODE_TOO_LONG")
  private String code;

  private String description;

  @NotNull(message = "SHIPPING_METHOD_BASE_FEE_REQUIRED")
  @DecimalMin(value = "0.0", inclusive = true, message = "SHIPPING_METHOD_BASE_FEE_INVALID")
  private BigDecimal baseFee;

  @DecimalMin(value = "0.0", inclusive = true, message = "SHIPPING_METHOD_FEE_PER_KM_INVALID")
  private BigDecimal feePerKm;

  @Min(value = 1, message = "SHIPPING_METHOD_ESTIMATED_DAYS_INVALID")
  private Integer estimatedDays;

  private String logoUrl;

  private Boolean isActive;
}
