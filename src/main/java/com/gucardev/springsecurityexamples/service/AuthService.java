package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.dto.*;
import com.gucardev.springsecurityexamples.event.UserRegisterEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  @Value("${application-details.password-reset-url}")
  private String passwordResetUrl;

  private final OTPService otpService;
  private final ApplicationEventPublisher eventPublisher;
  private final UserService userService;
  private final TokenService tokenService;
  private final AuthenticationManager authenticationManager;

  public TokenDto login(LoginRequest loginRequest) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(), loginRequest.getPassword()));
      return tokenService.generateTokenPairs(loginRequest.getUsername());
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }

  public TokenDto refreshToken(RefreshTokenRequest refreshTokenRequest) {
    return tokenService.generateTokenPairsViaRefreshToken(refreshTokenRequest.getRefreshToken());
  }

  public void logout(HttpServletRequest httpRequest) {
    String header = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      throw new RuntimeException("logout is not successful!");
    }
    var jwt = header.substring(7);
    tokenService.invalidateToken(jwt);
  }

  public void register(UserDto userDto) {
    var user = userService.createUser(userDto);
    eventPublisher.publishEvent(new UserRegisterEvent(this, user));
  }

  public void activateAccount(String username, String code) {
    otpService.verifyOTPForAccountActivate(username, code);
    var user = userService.getDtoByUsername(username);
    user.setEnabled(true);
    userService.updateUser(user);
  }

  public void resetPasswordRequest(PasswordResetRequest passwordResetRequest) {
    var user = userService.getDtoByEmail(passwordResetRequest.getEmail());
    var otp = otpService.createOTPForPasswordReset(user.getUsername());
    log.info(passwordResetUrl.formatted(otp.getUsername(), otp.getCode()));
  }
}
