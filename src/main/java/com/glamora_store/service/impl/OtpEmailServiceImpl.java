package com.glamora_store.service.impl;

import com.glamora_store.entity.Otp;
import com.glamora_store.entity.User;
import com.glamora_store.enums.ErrorMessage;
import com.glamora_store.enums.OtpPurpose;
import com.glamora_store.repository.OtpRepository;
import com.glamora_store.repository.UserRepository;
import com.glamora_store.service.OtpEmailService;
import com.glamora_store.util.ExceptionUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpEmailServiceImpl implements OtpEmailService {

  private final JavaMailSender mailSender;
  private final OtpRepository otpRepository;
  private final UserRepository userRepository;

  @Value("${spring.mail.username}")
  protected String emailSender;

  @Override
  public String sendOtp(String email, OtpPurpose purpose) {
    String otpCode = generateOtp();

    Otp otp = new Otp();
    otp.setEmail(email);
    otp.setOtp(otpCode);
    otp.setPurpose(purpose);
    otp.setExpiryTime(LocalDateTime.now().plusMinutes(15));
    otpRepository.save(otp);

    sendOtpEmail(email, otpCode, purpose);

    return otpCode;
  }

  private void sendOtpEmail(String recipientEmail, String otpCode, OtpPurpose purpose) {
    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);

      helper.setFrom(emailSender);
      helper.setTo(recipientEmail);
      helper.setSubject(purpose == OtpPurpose.REGISTER ? "Confirm Your Registration" : "OTP for Password Reset");

      String emailContent =
        "<div style=\"font-family: Arial, sans-serif; max-width:600px; margin:auto; padding:20px; border:1px solid #eee; border-radius:8px;\">"
          + "<h2 style=\"color:#00466a; text-align:center;\">Glamora Store</h2>"
          + "<p>Hi,</p>"
          + "<p>Use the following OTP to "
          + (purpose == OtpPurpose.REGISTER ? "complete your registration" : "reset your password")
          + ". OTP is valid for 15 minutes.</p>"
          + "<div style=\"text-align:center; margin:20px 0;\">"
          + "<span style=\"font-size:32px; font-weight:bold; color:#fff; background-color:#00466a; padding:10px 20px; border-radius:6px;\">"
          + otpCode + "</span>"
          + "</div>"
          + "<p>Regards,<br/>Glamora Store Team</p>"
          + "<hr style=\"border:none; border-top:1px solid #eee; margin-top:20px;\"/>"
          + "<p style=\"font-size:12px; color:#aaa; text-align:center;\">Glamora Inc, 1600 Amphitheatre Parkway, California</p>"
          + "</div>";

      helper.setText(emailContent, true); // true = HTML

      mailSender.send(message);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateOtp() {
    SecureRandom random = new SecureRandom();
    int otp = 100000 + random.nextInt(900000);
    return String.valueOf(otp);
  }

  @Override
  @Transactional
  public boolean verifyOtp(String email, String otpCode, OtpPurpose purpose) {
    Optional<Otp> otpOptional = otpRepository.findByEmailAndOtpAndPurpose(email, otpCode, purpose);
    if (otpOptional.isEmpty()) return false;

    Otp otp = otpOptional.get();
    if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
      otpRepository.delete(otp);
      return false;
    }

    otpRepository.deleteAllByEmailAndPurpose(email, purpose);

    // Activate User
    if (purpose == OtpPurpose.REGISTER) {
      User user = userRepository
        .findByEmail(email)
        .orElseThrow(() -> ExceptionUtil.notFound(ErrorMessage.USER_NOT_FOUND));

      user.setIsDeleted(false);

      userRepository.save(user);
    }
    return true;
  }
}
