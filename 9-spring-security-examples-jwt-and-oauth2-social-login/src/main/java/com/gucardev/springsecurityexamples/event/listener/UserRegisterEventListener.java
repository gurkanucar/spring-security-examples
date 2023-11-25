package com.gucardev.springsecurityexamples.event.listener;

import com.gucardev.springsecurityexamples.event.UserRegisterEvent;
import com.gucardev.springsecurityexamples.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegisterEventListener implements ApplicationListener<UserRegisterEvent> {

  private final OTPService otpService;

  @Value("${application-details.account-activate-url}")
  private String accountActivateUrl;

  @Override
  public void onApplicationEvent(UserRegisterEvent event) {
    log.info("user registered: {}", event.getUser().getUsername());
    var otp = otpService.createOTPForAccountActivate(event.getUser().getUsername());
    log.info("activate url: {}", accountActivateUrl.formatted(otp.getUsername(), otp.getCode()));
  }
}
