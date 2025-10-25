package com.glamora_store.dto.request.common.address;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressCreateRequest {

  @NotBlank(message = "RECEIVER_NAME_REQUIRED")
  private String receiverName;

  @NotBlank(message = "RECEIVER_PHONE_REQUIRED")
  @Pattern(regexp = "^(0|\\+84)(3|5|7|8|9)[0-9]{8}$", message = "PHONE_NUMBER_INVALID")
  private String receiverPhone;

  @NotBlank(message = "PROVINCE_REQUIRED")
  private String province;

  @NotBlank(message = "DISTRICT_REQUIRED")
  private String district;

  @NotBlank(message = "WARD_REQUIRED")
  private String ward;

  @NotBlank(message = "STREET_DETAIL_REQUIRED")
  private String streetDetail;

  private Double latitude;

  private Double longitude;

  @JsonProperty("default")
  @Builder.Default
  private boolean isDefault = false;
}
