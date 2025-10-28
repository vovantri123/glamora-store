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

  @Value("${frontend.url}")
  private String frontendUrl;

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
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(emailSender);
      helper.setTo(recipientEmail);
      helper.setSubject(purpose == OtpPurpose.REGISTER ? "Confirm Your Registration" : "Reset Your Password");

      // URL encode email
      String encodedEmail = java.net.URLEncoder.encode(recipientEmail, java.nio.charset.StandardCharsets.UTF_8);
      String verifyUrl = purpose == OtpPurpose.REGISTER
          ? frontendUrl + "/verify-otp?email=" + encodedEmail
          : frontendUrl + "/reset-password?email=" + encodedEmail;

      String emailContent = "<!DOCTYPE html>"
          + "<html lang=\"en\">"
          + "<head>"
          + "<meta charset=\"UTF-8\">"
          + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
          + "<title>" + (purpose == OtpPurpose.REGISTER ? "Verify Your Email" : "Reset Your Password") + "</title>"
          + "</head>"
          + "<body style=\"margin:0; padding:0; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; background-color:#f5f5f5;\">"
          + "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#f5f5f5; padding:40px 20px;\">"
          + "<tr><td align=\"center\">"
          + "<table width=\"600\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.1);\">"

          // Header with gradient
          + "<tr><td style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding:40px 20px; text-align:center;\">"
          + "<h1 style=\"color:#ffffff; margin:0; font-size:28px; font-weight:700; letter-spacing:-0.5px;\">✨ Glamora Store</h1>"
          + "</td></tr>"

          // Body content
          + "<tr><td style=\"padding:40px 30px;\">"
          + "<h2 style=\"color:#1f2937; margin:0 0 20px 0; font-size:24px; font-weight:600;\">"
          + (purpose == OtpPurpose.REGISTER ? "Welcome! Verify Your Email" : "Reset Your Password")
          + "</h2>"
          + "<p style=\"color:#6b7280; font-size:16px; line-height:1.6; margin:0 0 24px 0;\">"
          + (purpose == OtpPurpose.REGISTER
              ? "Thank you for registering with Glamora Store! Please use the OTP code below to complete your registration."
              : "We received a request to reset your password. Use the OTP code below to proceed.")
          + "</p>"

          // OTP Box with modern design
          + "<div style=\"background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); border-radius:12px; padding:30px; text-align:center; margin:24px 0;\">"
          + "<p style=\"color:#ffffff; font-size:14px; margin:0 0 12px 0; font-weight:500; letter-spacing:1px; text-transform:uppercase;\">Your OTP Code</p>"
          + "<div style=\"background-color:rgba(255,255,255,0.2); border-radius:8px; padding:20px; display:inline-block;\">"
          + "<span style=\"font-size:42px; font-weight:700; color:#ffffff; letter-spacing:8px; font-family:'Courier New', monospace;\">"
          + otpCode
          + "</span>"
          + "</div>"
          + "<p style=\"color:#e5e7eb; font-size:13px; margin:12px 0 0 0;\">⏰ Valid for 15 minutes</p>"
          + "</div>"

          // Call to action button
          + "<div style=\"text-align:center; margin:32px 0;\">"
          + "<a href=\"" + verifyUrl
          + "\" style=\"display:inline-block; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color:#ffffff; text-decoration:none; padding:14px 32px; border-radius:8px; font-weight:600; font-size:16px; box-shadow: 0 4px 6px rgba(102, 126, 234, 0.3);\">"
          + (purpose == OtpPurpose.REGISTER ? "Verify Email Now" : "Reset Password Now")
          + "</a>"
          + "</div>"

          + "<p style=\"color:#9ca3af; font-size:14px; line-height:1.6; margin:24px 0 0 0;\">"
          + "If the button doesn't work, copy and paste this link into your browser:<br/>"
          + "<a href=\"" + verifyUrl + "\" style=\"color:#667eea; word-break:break-all;\">" + verifyUrl + "</a>"
          + "</p>"

          + "<div style=\"margin-top:32px; padding-top:24px; border-top:1px solid #e5e7eb;\">"
          + "<p style=\"color:#9ca3af; font-size:13px; line-height:1.6; margin:0;\">"
          + "💡 <strong>Tip:</strong> If you didn't request this "
          + (purpose == OtpPurpose.REGISTER ? "registration" : "password reset") + ", please ignore this email."
          + "</p>"
          + "</div>"
          + "</td></tr>"

          // Footer
          + "<tr><td style=\"background-color:#f9fafb; padding:30px; text-align:center; border-top:1px solid #e5e7eb;\">"
          + "<p style=\"color:#6b7280; font-size:14px; margin:0 0 8px 0;\">Best regards,<br/><strong>Glamora Store Team</strong></p>"
          + "<div style=\"margin:20px 0;\">"
          + "<a href=\"" + frontendUrl
          + "\" style=\"color:#667eea; text-decoration:none; margin:0 10px; font-size:13px;\">Visit Store</a>"
          + "<span style=\"color:#d1d5db;\">•</span>"
          + "<a href=\"mailto:" + emailSender
          + "\" style=\"color:#667eea; text-decoration:none; margin:0 10px; font-size:13px;\">Contact Us</a>"
          + "</div>"
          + "<p style=\"color:#9ca3af; font-size:12px; margin:16px 0 0 0;\">"
          + "© 2025 Glamora Store. All rights reserved.<br/>"
          + "1600 Amphitheatre Parkway, California"
          + "</p>"
          + "</td></tr>"

          + "</table>"
          + "</td></tr>"
          + "</table>"
          + "</body>"
          + "</html>";

      helper.setText(emailContent, true); // true = HTML

      mailSender.send(message);
    } catch (Exception e) {
      log.error("Failed to send OTP email to {}: {}", recipientEmail, e.getMessage());
      throw new RuntimeException(ErrorMessage.SEND_EMAIL_FAIL.getMessage(), e);
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
    if (otpOptional.isEmpty())
      return false;

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
