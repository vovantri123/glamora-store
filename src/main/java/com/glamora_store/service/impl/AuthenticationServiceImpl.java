package com.glamora_store.service.impl;

import com.glamora_store.dto.request.AuthenticationRequest;
import com.glamora_store.dto.request.IntrospectRequest;
import com.glamora_store.dto.response.AuthenticationResponse;
import com.glamora_store.dto.response.IntrospectResponse;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorCode;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.AuthenticationService;
import com.glamora_store.util.ExceptionUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;

  @Value("${jwt.secretKey}")
  protected String SECRET_KEY;

  public AuthenticationResponse authenticate(AuthenticationRequest request)
    throws JOSEException {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    User user = userRepository.findByEmailAndIsDeletedFalse(request.getEmail())
      .orElseThrow(() -> ExceptionUtil.badRequest(ErrorCode.USER_NOT_EXISTED));

    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!authenticated) {
      throw ExceptionUtil.badRequest(ErrorCode.UNAUTHENTICATED);
    }

    String token = generateToken(user);

    return AuthenticationResponse.builder()
      .token(token)
      .authenticated(true)
      .build();
  }

  private String generateToken(User user) throws JOSEException {
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
      .subject(user.getEmail()) // subject mà token đại diện.
      .issuer("https://glamora-store.com")
      .issueTime(new Date())
      .expirationTime(new Date(
        Instant.now().plus(30, ChronoUnit.MINUTES).toEpochMilli()
      ))
      .claim("scope", buildScope(user))
      .claim("Custom_key", "Custom_value")
      .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);

    jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
    return jwsObject.serialize();
  }

  private String buildScope(User user) {
    // Lúc trả ra JWT sau khi người dùng authenticate
    // Dùng Set để loại bỏ trùng Permission, LinkedHashSet để đảm bảo Role đứng trước Permission
    Set<String> scopes = new LinkedHashSet<>();

    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        // Role trước
        scopes.add("ROLE_" + role.getName());

        // Permission sau
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions()
            .forEach(permission -> scopes.add(permission.getName()));
        }
      });
    }

    return String.join(" ", scopes);
  }

  public IntrospectResponse introspect(IntrospectRequest request)
    throws JOSEException, ParseException {
    String token = request.getToken();

    JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(token);

    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    boolean verified = signedJWT.verify(verifier);

    return IntrospectResponse.builder()
      .valid(verified && expiryTime.after(new Date()))
      .build();
  }

}
