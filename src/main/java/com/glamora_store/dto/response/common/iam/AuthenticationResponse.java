package com.glamora_store.dto.response.common.iam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
  private String accessToken;
  private String refreshToken;

  @JsonProperty("expiresIn")
  private Long expiresIn; // Access token expiration time in seconds

  @JsonProperty("refreshExpiresIn")
  private Long refreshExpiresIn; // Refresh token expiration time in seconds
}
