package com.glamora_store.service.impl;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.enums.ErrorCode;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.AuthenticationService;
import com.glamora_store.util.ExceptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;

  public Boolean authenticate(AuthenticationRequest request) {
    var user = userRepository.findByEmail(request.getEmail())
      .orElseThrow(() -> ExceptionUtil.badRequest(ErrorCode.USER_NOT_EXISTED));

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    return passwordEncoder.matches(request.getPassword(), user.getPassword());
  }
}
