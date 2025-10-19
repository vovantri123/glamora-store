package com.glamora_store.repository;

import com.glamora_store.entity.Otp;
import com.glamora_store.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
  Optional<Otp> findByEmailAndOtpAndPurpose(String email, String otp, OtpPurpose purpose);

  void deleteAllByEmailAndPurpose(String email, OtpPurpose purpose);
}
