package com.glamora_store.dto.response.common.address;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {

  private Long id;
  private String receiverName;
  private String receiverPhone;
  private String province;
  private String district;
  private String ward;
  private String streetDetail;
  private Double latitude;
  private Double longitude;
  private boolean isDefault;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
