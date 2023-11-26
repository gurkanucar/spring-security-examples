package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.model.OTP;
import com.gucardev.springsecurityexamples.model.OTPType;
import com.gucardev.springsecurityexamples.repository.OTPRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OTPService {

  private final OTPRepository otpRepository;

  @Value("${otp-variables.EXPIRES_ACC_ACTIVATE_CODE_MINUTE}")
  private long expiresAccActivateCodeDuration;

  @Value("${otp-variables.EXPIRES_PASSWORD_RESET_CODE_MINUTE}")
  private long expiresPasswordResetCodeDuration;

  public OTP createOTP(String username, OTPType otpType, long expiresDuration, String code) {
    var existing =
        otpRepository.findByUsernameAndOtpTypeOrderByCreatedDateTimeDesc(username, otpType);
    existing.ifPresent(otpRepository::delete);

    Instant expirationTime = null;
    boolean expireNever = false;
    if (expiresDuration == -1) {
      expireNever = true;
    } else {
      expirationTime = Instant.now().plus(Duration.ofMinutes(expiresDuration));
    }

    var otp =
        otpRepository.save(
            OTP.builder()
                .otpType(otpType)
                .expireNever(expireNever)
                .expiryDate(expirationTime)
                .username(username)
                .code(code)
                .build());
    log.info(otp.toString());
    return otp;
  }

  public Optional<OTP> findByUsernameAndType(String username, OTPType otpType) {
    return otpRepository.findByUsernameAndOtpTypeOrderByCreatedDateTimeDesc(username, otpType);
  }

  public void verifyOTP(String username, String code, OTPType otpType) {
    var existing =
        otpRepository.findByCodeAndUsernameAndOtpTypeOrderByCreatedDateTimeDesc(
            code, username, otpType);
    existing.ifPresentOrElse(
        otp -> {
          verifyOTPExpiration(otp);
          otpRepository.delete(otp);
        },
        () -> {
          throw new RuntimeException("OTP not found!");
        });
  }

  public void verifyOTPExpiration(OTP otp) {
    if (!otp.isExpireNever() && otp.getExpiryDate().isBefore(Instant.now())) {
      throw new RuntimeException("OTP has expired and cannot be used!");
    }
  }

  public OTP createOTPForAccountActivate(String username) {
    return createOTP(
        username,
        OTPType.ACC_ACTIVATION,
        expiresAccActivateCodeDuration,
        UUID.randomUUID().toString());
  }

  public OTP createOTPForPasswordReset(String username) {
    return createOTP(
        username,
        OTPType.PASSWORD_RESET,
        expiresPasswordResetCodeDuration,
        generateRandomCode().toString());
  }

  public void verifyOTPForAccountActivate(String username, String code) {
    verifyOTP(username, code, OTPType.ACC_ACTIVATION);
  }

  public void verifyOTPForPasswordReset(String username, String code) {
    verifyOTP(username, code, OTPType.PASSWORD_RESET);
  }

  public Integer generateRandomCode() {
    int max = 99999;
    int min = 10000;
    SecureRandom r = new SecureRandom();
    return r.nextInt(max - min) + min;
  }
}
