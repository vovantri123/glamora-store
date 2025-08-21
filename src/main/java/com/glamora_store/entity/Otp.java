package com.glamora_store.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.glamora_store.enums.OtpPurpose;

import lombok.*;

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
