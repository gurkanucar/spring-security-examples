package com.gucardev.springsecurityexamples.repository;

import com.gucardev.springsecurityexamples.model.OTP;
import com.gucardev.springsecurityexamples.model.OTPType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, Long> {

  Optional<OTP> findByCode(String code);

  Optional<OTP> findByCodeAndUsernameAndOtpTypeOrderByCreatedDateTimeDesc(
      String code, String username, OTPType otpType);

  Optional<OTP> findByUsernameAndOtpTypeOrderByCreatedDateTimeDesc(
      String username, OTPType otpType);
}
