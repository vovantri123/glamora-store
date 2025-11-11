package com.glamora_store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 500)
    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private Instant issuedAt; // Thời điểm token được tạo

    @Builder.Default
    @Column(nullable = false)
    private Boolean revoked = false;

    // IP address and user agent for security tracking
    @Column(length = 45)
    private String ipAddress;

    @Column(length = 500)
    private String userAgent;
}
