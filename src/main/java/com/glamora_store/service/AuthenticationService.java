package com.glamora_store.service;

import java.text.ParseException;

import com.glamora_store.dto.request.iam.AuthenticationRequest;
import com.glamora_store.dto.request.iam.IntrospectRequest;
import com.glamora_store.dto.response.iam.AuthenticationResponse;
import com.glamora_store.dto.response.iam.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
