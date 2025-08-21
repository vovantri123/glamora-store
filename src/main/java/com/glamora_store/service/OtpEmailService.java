package com.glamora_store.service;

import com.glamora_store.enums.OtpPurpose;

public interface OtpEmailService {
    String sendOtp(String email, OtpPurpose purpose);

    boolean verifyOtp(String email, String otp, OtpPurpose purpose);
}
