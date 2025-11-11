package com.glamora_store.repository;

import com.glamora_store.entity.RefreshToken;
import com.glamora_store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    Optional<RefreshToken> findByRefreshTokenAndRevokedFalse(String refreshToken);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.user = :user")
    void revokeAllUserTokens(User user);

    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true WHERE rt.refreshToken = :refreshToken")
    void revokeToken(String refreshToken);

    void deleteByExpiryDateBefore(Instant now);

    boolean existsByRefreshTokenAndRevokedFalse(String refreshToken);
}
