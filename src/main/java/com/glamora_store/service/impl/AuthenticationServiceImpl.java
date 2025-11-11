package com.glamora_store.service.impl;

import com.glamora_store.dto.request.common.iam.AuthenticationRequest;
import com.glamora_store.dto.request.common.iam.IntrospectRequest;
import com.glamora_store.dto.request.common.iam.LogoutRequest;
import com.glamora_store.dto.request.common.iam.RefreshTokenRequest;
import com.glamora_store.dto.response.common.iam.AuthenticationResponse;
import com.glamora_store.entity.RefreshToken;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OtpPurpose;
import com.glamora_store.repository.OtpRepository;
import com.glamora_store.repository.RefreshTokenRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.AuthenticationService;
import com.glamora_store.util.ExceptionUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
  private final UserRepository userRepository;
  private final OtpRepository otpRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Value("${jwt.secretKey}")
  protected String SECRET_KEY;

  @Value("${jwt.access-token-expiration}")
  protected Long ACCESS_TOKEN_EXPIRATION;

  @Value("${jwt.refresh-token-expiration}")
  protected Long REFRESH_TOKEN_EXPIRATION;

  @Override
  @Transactional
  public AuthenticationResponse authenticate(AuthenticationRequest request, HttpServletRequest httpRequest) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    // Check if user exists
    User user = userRepository
        .findByEmailAndIsDeletedFalse(request.getEmail())
        .orElseThrow(() -> ExceptionUtil.badRequest(ErrorMessage.EMAIL_NOT_REGISTERED));

    // Check if user has been deleted/deactivated
    if (user.getIsDeleted()) {
      throw ExceptionUtil.badRequest(ErrorMessage.USER_DELETED);
    }

    // Check if account is verified (no pending OTP for REGISTER)
    boolean hasUnverifiedOtp = otpRepository.existsByEmailAndPurpose(
        request.getEmail(),
        OtpPurpose.REGISTER);
    if (hasUnverifiedOtp) {
      throw ExceptionUtil.badRequest(ErrorMessage.ACCOUNT_NOT_VERIFIED);
    }

    // Check password
    boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
    if (!authenticated) {
      throw ExceptionUtil.badRequest(ErrorMessage.PASSWORD_INCORRECT);
    }

    // Revoke all existing refresh tokens for this user
    refreshTokenRepository.revokeAllUserTokens(user);

    // Generate tokens
    String accessToken = generateAccessToken(user);
    String refreshToken = generateRefreshToken(user, httpRequest);

    return AuthenticationResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .expiresIn(ACCESS_TOKEN_EXPIRATION)
        .refreshExpiresIn(REFRESH_TOKEN_EXPIRATION)
        .build();
  }

  @Override
  @Transactional
  public AuthenticationResponse refreshToken(RefreshTokenRequest request, HttpServletRequest httpRequest) {
    String token = request.getRefreshToken();

    // Find refresh token
    RefreshToken refreshToken = refreshTokenRepository
        .findByRefreshTokenAndRevokedFalse(token)
        .orElseThrow(() -> ExceptionUtil.badRequest(ErrorMessage.INVALID_REFRESH_TOKEN));

    // Check if token is expired
    if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
      refreshTokenRepository.revokeToken(token);
      throw ExceptionUtil.badRequest(ErrorMessage.REFRESH_TOKEN_EXPIRED);
    }

    User user = refreshToken.getUser();

    // Check if user is still active
    if (user.getIsDeleted()) {
      throw ExceptionUtil.badRequest(ErrorMessage.USER_DELETED);
    }

    // Revoke old refresh token
    refreshTokenRepository.revokeToken(token);

    // Generate new tokens
    String newAccessToken = generateAccessToken(user);
    String newRefreshToken = generateRefreshToken(user, httpRequest);

    return AuthenticationResponse.builder()
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .expiresIn(ACCESS_TOKEN_EXPIRATION)
        .refreshExpiresIn(REFRESH_TOKEN_EXPIRATION)
        .build();
  }

  @Override
  @Transactional
  public void logout(LogoutRequest request) {
    String token = request.getRefreshToken();

    refreshTokenRepository
        .findByRefreshToken(token)
        .orElseThrow(() -> ExceptionUtil.badRequest(ErrorMessage.INVALID_REFRESH_TOKEN));

    refreshTokenRepository.revokeToken(token);
  }

  private String generateAccessToken(User user) {
    try {
      JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

      JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
          .subject(user.getEmail()) // subject mà token đại diện.
          .issuer("https://glamora-store.com")
          .issueTime(new Date())
          .expirationTime(
              new Date(Instant.now().plus(ACCESS_TOKEN_EXPIRATION, ChronoUnit.SECONDS).toEpochMilli()))
          .claim("scope", buildScope(user))
          .claim("userId", user.getId())
          .claim("Custom_key", "Custom_value")
          .jwtID(UUID.randomUUID().toString())
          .build();

      Payload payload = new Payload(jwtClaimsSet.toJSONObject());

      JWSObject jwsObject = new JWSObject(header, payload);

      jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
  }

  private String generateRefreshToken(User user, HttpServletRequest httpRequest) {
    String token = UUID.randomUUID().toString();
    Instant now = Instant.now();

    RefreshToken refreshToken = RefreshToken.builder()
        .refreshToken(token)
        .user(user)
        .issuedAt(now)
        .expiryDate(now.plus(REFRESH_TOKEN_EXPIRATION, ChronoUnit.SECONDS))
        .revoked(false)
        .ipAddress(getClientIpAddress(httpRequest))
        .userAgent(httpRequest.getHeader("User-Agent"))
        .build();

    refreshTokenRepository.save(refreshToken);
    return token;
  }

  private String getClientIpAddress(HttpServletRequest request) {
    String xForwardedForHeader = request.getHeader("X-Forwarded-For");
    if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
      return xForwardedForHeader.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }

  private String buildScope(User user) {
    // Lúc trả ra JWT sau khi người dùng authenticate
    // Dùng Set để loại bỏ trùng Permission, LinkedHashSet để đảm bảo Role đứng
    // trước Permission
    Set<String> scopes = new LinkedHashSet<>();

    if (!CollectionUtils.isEmpty(user.getRoles())) {
      user.getRoles().forEach(role -> {
        // Role trước
        scopes.add("ROLE_" + role.getName());

        // Permission sau
        if (!CollectionUtils.isEmpty(role.getPermissions())) {
          role.getPermissions().forEach(permission -> scopes.add(permission.getName()));
        }
      });
    }

    return String.join(" ", scopes);
  }

  @Override
  public Boolean introspect(IntrospectRequest request) throws JOSEException, ParseException {
    String accessToken = request.getAccessToken();

    JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

    SignedJWT signedJWT = SignedJWT.parse(accessToken);

    Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

    boolean verified = signedJWT.verify(verifier);

    return verified && expiryTime.after(new Date());
  }
}
