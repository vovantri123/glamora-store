package com.glamora_store.controller;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final AuthenticationService authenticationService;

  @PostMapping("/log-in")
  ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
    AuthenticationResponse response = new AuthenticationResponse(authenticationService.authenticate(request));

    return new ApiResponse<>(
      HttpStatus.OK.value(),
      SuccessMessage.LOGIN_SUCCESS.getMessage(),
      response
    );
  }
}
