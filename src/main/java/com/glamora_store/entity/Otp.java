package com.glamora_store.entity;

import com.glamora_store.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "`otp`")
public class Otp {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String email;
  private String otp;

  @Enumerated(EnumType.STRING)
  private OtpPurpose purpose; // dùng enum thay vì String

  private LocalDateTime expiryTime;
}
