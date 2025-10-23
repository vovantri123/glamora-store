package com.glamora_store.dto.response.common.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingMethodResponse {
  private Long id;
  private String name;
  private String code;
  private String description;
  private BigDecimal baseFee;
  private BigDecimal feePerKm;
  private Integer estimatedDays;
  private String logoUrl;
  private Boolean isActive;
}
