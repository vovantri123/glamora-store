package com.glamora_store.controller;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.request.IntrospectRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;
  private final RestClient.Builder builder;

  @PostMapping("/token")
  ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request)
    throws JOSEException {
    AuthenticationResponse response = authenticationService.authenticate(request);

    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.LOGIN_SUCCESS.getMessage(),
      response
    );
  }

  @PostMapping("/introspect")
  ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
    throws ParseException, JOSEException {
    IntrospectResponse result = authenticationService.introspect(request);

    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.TOKEN_VALIDATION_SUCCESS.getMessage(),
      result
    );
  }
}
