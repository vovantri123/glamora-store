package com.glamora_store.service;

import java.text.ParseException;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.request.IntrospectRequest;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
}
