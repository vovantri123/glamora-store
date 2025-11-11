package com.glamora_store.service;

import com.glamora_store.dto.request.common.iam.AuthenticationRequest;
import com.glamora_store.dto.request.common.iam.IntrospectRequest;
import com.glamora_store.dto.request.common.iam.LogoutRequest;
import com.glamora_store.dto.request.common.iam.RefreshTokenRequest;
import com.glamora_store.dto.response.common.iam.AuthenticationResponse;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpRequest);

  AuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest);

  void logout(LogoutRequest request);

  Boolean introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
