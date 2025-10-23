package com.glamora_store.dto.request.admin.shipping;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShippingMethodRequest {

  @Size(max = 100, message = "SHIPPING_METHOD_NAME_TOO_LONG")
  private String name;

  private String description;

  @DecimalMin(value = "0.0", inclusive = true, message = "SHIPPING_METHOD_BASE_FEE_INVALID")
  private BigDecimal baseFee;

  @DecimalMin(value = "0.0", inclusive = true, message = "SHIPPING_METHOD_FEE_PER_KM_INVALID")
  private BigDecimal feePerKm;

  @Min(value = 1, message = "SHIPPING_METHOD_ESTIMATED_DAYS_INVALID")
  private Integer estimatedDays;

  private String logoUrl;

  private Boolean isActive;
}
