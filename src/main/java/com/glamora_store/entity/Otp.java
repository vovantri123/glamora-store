package com.glamora_store.entity;

import com.glamora_store.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "otp")
public class Otp {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String otp;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OtpPurpose purpose;

  @Column(name = "expiry_time", nullable = false)
  private LocalDateTime expiryTime;

  @Column(name = "is_used")
  @Builder.Default
  private Boolean isUsed = false;
}
