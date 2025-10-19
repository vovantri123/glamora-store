package com.glamora_store.service;

import com.glamora_store.dto.request.iam.AuthenticationRequest;
import com.glamora_store.dto.request.iam.IntrospectRequest;
import com.glamora_store.dto.response.iam.AuthenticationResponse;
import com.glamora_store.dto.response.iam.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request);

  IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
