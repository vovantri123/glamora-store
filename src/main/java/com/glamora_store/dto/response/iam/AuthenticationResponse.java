package com.glamora_store.dto.response.iam;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;
    private Boolean authenticated;
}
