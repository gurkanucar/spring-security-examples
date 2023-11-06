package com.gucardev.springsecurityexamples.event.listener;

import com.gucardev.springsecurityexamples.event.UserRegisterEvent;
import com.gucardev.springsecurityexamples.service.OTPService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegisterEventListener implements ApplicationListener<UserRegisterEvent> {

  private final OTPService otpService;

  @Override
  public void onApplicationEvent(UserRegisterEvent event) {
    log.info("user registered: {}", event.getUser().getUsername());
    otpService.createOTPForAccountActivate(event.getUser().getUsername());
  }
}
