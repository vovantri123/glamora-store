package com.glamora_store.service;

import com.glamora_store.dto.request.AuthenticationRequest;

public interface AuthenticationService {
  Boolean authenticate(AuthenticationRequest request);
}
