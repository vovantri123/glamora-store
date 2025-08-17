package com.glamora_store.service;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.request.IntrospectRequest;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException;

  IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}

