package com.glamora_store.service;

import com.glamora_store.dto.request.common.iam.AuthenticationRequest;
import com.glamora_store.dto.request.common.iam.IntrospectRequest;
import com.glamora_store.dto.response.common.iam.AuthenticationResponse;
import com.glamora_store.dto.response.common.iam.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
  AuthenticationResponse authenticate(AuthenticationRequest request);

  IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
