package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.model.OTP;
import com.gucardev.springsecurityexamples.model.OTPType;
import com.gucardev.springsecurityexamples.repository.OTPRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OTPService {

  @Value("${otp-variables.EXPIRES_ACC_ACTIVATE_CODE_MINUTE}")
  private long expiresAccActivateCodeDuration;

  @Value("${otp-variables.EXPIRES_PASSWORD_RESET_CODE_MINUTE}")
  private long expiresPasswordResetCodeDuration;

  private final OTPRepository otpRepository;

  public OTP createOTP(String username, OTPType otpType, long expiresDuration) {
    var existing =
        otpRepository.findByUsernameAndOtpTypeOrderByCreatedDateTimeDesc(username, otpType);
    existing.ifPresent(otpRepository::delete);

    Instant expirationTime;
    if (expiresDuration == -1) {
      // Set expirationTime to a far future date (effectively infinite)
      expirationTime = Instant.MAX;
    } else {
      expirationTime = Instant.now().plus(Duration.ofMinutes(expiresDuration));
    }

    var otp =
        otpRepository.save(
            OTP.builder()
                .otpType(otpType)
                .expiryDate(expirationTime)
                .username(username)
                .code(generateRandomCode().toString())
                .build());
    log.info(otp.toString());
    return otp;
  }

  public boolean verifyOTP(String username, String code, OTPType otpType) {
    var existing =
        otpRepository.findByCodeAndUsernameAndOtpTypeOrderByCreatedDateTimeDesc(
            code, username, otpType);
    existing.ifPresent(
        otp -> {
          verifyOTP(otp);
          otpRepository.delete(otp);
        });
    return true;
  }

  public void verifyOTP(OTP otp) {
    if (otp.getExpiryDate().isBefore(Instant.now())) {
      throw new RuntimeException("OTP has expired and cannot be used!");
    }
  }

  public OTP createOTPForAccountActivate(String username) {
    return createOTP(username, OTPType.ACC_ACTIVATION, expiresAccActivateCodeDuration);
  }

  public OTP createOTPForPasswordReset(String username) {
    return createOTP(username, OTPType.PASSWORD_RESET, expiresPasswordResetCodeDuration);
  }

  public boolean verifyOTPForAccountActivate(String username, String code) {
    return verifyOTP(username, code, OTPType.ACC_ACTIVATION);
  }

  public boolean verifyOTPForPasswordReset(String username, String code) {
    return verifyOTP(username, code, OTPType.PASSWORD_RESET);
  }

  public Integer generateRandomCode() {
    int max = 99999;
    int min = 10000;
    SecureRandom r = new SecureRandom();
    return r.nextInt(max - min) + min;
  }
}
