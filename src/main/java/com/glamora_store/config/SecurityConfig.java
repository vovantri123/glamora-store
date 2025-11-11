package com.glamora_store.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  // spotless:off
  public static final String[] PUBLIC_ENDPOINTS = {
      "/api-docs",
      "/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v3/api-docs/**",

      "/public/**",

      // VNPay callback endpoint - VNPay will redirect here after payment
      "/user/payments/vnpay-return"
  };

  public static final String[] USER_ENDPOINTS = {
      "/user/**"
  };

  public static final String[] ADMIN_ENDPOINTS = {
      "/admin/**"
  };
  // spotless:on

  @Value("${jwt.secretKey}")
  protected String SECRET_KEY;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        // CorsFilter chỉ áp dụng cho request thành công: CorsConfig.java tạo
        // CorsFilter, nhưng nó chạy sau Spring Security filter
        // Nên cần thêm CORS headers vào TẤT CẢ response, kể cả lỗi 401/403.
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(request -> request
            // Allow CORS preflight requests (OPTIONS)
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
            .requestMatchers(USER_ENDPOINTS).hasAnyRole("USER", "ADMIN")
            .requestMatchers(ADMIN_ENDPOINTS).hasRole("ADMIN")
            .anyRequest().authenticated());

    httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
        jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));

    httpSecurity.csrf(AbstractHttpConfigurer::disable);

    return httpSecurity.build();
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    // Xóa prefix mặc định "SCOPE_" khi Spring Security convert claim trong JWT
    // thành GrantedAuthority
    // (giữ nguyên ROLE_ADMIN thay vì SCOPE_ROLE_ADMIN). Lúc Spring Security đọc JWT

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

    return jwtAuthenticationConverter;
  }

  @Bean
  JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS512");

    NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build();

    // Disable clock skew (default is 60 seconds) to check expiration immediately
    /*
     * Clock skew (hay clock drift) là sự chênh lệch thời gian giữa các server trong
     * hệ thống phân tán. Ví dụ:
     * 
     * Server A: 10:00:00
     * Server B: 10:00:05 (chênh 5 giây)
     * Khi JWT có exp (expiration time) = 10:00:00, server A sẽ reject ngay lập tức,
     * nhưng server B vẫn chấp nhận trong 5 giây nữa.
     */
    decoder.setJwtValidator(new org.springframework.security.oauth2.jwt.JwtTimestampValidator(
        java.time.Duration.ZERO));

    return decoder;
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
