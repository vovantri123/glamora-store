package com.glamora_store.dto.response.common.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodResponse {

  private Long id;
  private String name;
  private String description;
  private String logoUrl;
  private Boolean isActive;
}
