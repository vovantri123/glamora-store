package com.glamora_store.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.request.IntrospectRequest;
import com.glamora_store.dto.response.ApiResponse;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.glamora_store.enums.SuccessMessage;
import com.glamora_store.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RestClient.Builder builder;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request)
            throws JOSEException {
        AuthenticationResponse response = authenticationService.authenticate(request);

        return new ApiResponse<>(HttpStatus.OK.value(), SuccessMessage.LOGIN_SUCCESS.getMessage(), response);
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        IntrospectResponse result = authenticationService.introspect(request);

        String message = result.isValid()
                ? SuccessMessage.TOKEN_VALIDATION_SUCCESS.getMessage()
                : SuccessMessage.TOKEN_VALIDATION_FAILURE.getMessage();

        return new ApiResponse<>(HttpStatus.OK.value(), message, result);
    }
}
