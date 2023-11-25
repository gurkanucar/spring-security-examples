package com.gucardev.springsecurityexamples.controller;

import com.gucardev.springsecurityexamples.dto.*;
import com.gucardev.springsecurityexamples.service.AuthService;
import com.gucardev.springsecurityexamples.validation.CreateValidationGroup;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Void> register(
      @Validated(CreateValidationGroup.class) @RequestBody UserDto userDto) {
    authService.register(userDto);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().body(authService.login(loginRequest));
  }

  @PostMapping("/logout")
  public ResponseEntity<TokenDto> logout(HttpServletRequest httpRequest) {
    authService.logout(httpRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<TokenDto> refreshToken(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok().body(authService.refreshToken(refreshTokenRequest));
  }

  @PostMapping("/forget-password")
  public ResponseEntity<Void> forgetPassword(
      @Valid @RequestBody PasswordForgetRequest passwordForgetRequest) {
    authService.resetPasswordRequest(passwordForgetRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/set-new-password")
  public ResponseEntity<Void> setNewPassword(
      @Valid @RequestBody SetNewPasswordRequest setNewPasswordRequest) {
    authService.setNewPassword(setNewPasswordRequest);
    return ResponseEntity.ok().build();
  }

  @RequestMapping("/activate-account")
  public ModelAndView activateAccount(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "username", required = false) String username) {
    ModelAndView mav;
    try {
      mav = new ModelAndView("activationSuccess");
      authService.activateAccount(username, code);
      return mav;
    } catch (Exception e) {
      mav = new ModelAndView("activationError");
      mav.addObject("error", "Error activating the account: " + e.getMessage());
      return mav;
    }
  }

  @RequestMapping("/set-new-password/ui")
  public ModelAndView showResetForm(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "username", required = false) String username) {
    ModelAndView mav = new ModelAndView("resetPassword");
    mav.addObject("username", username);
    mav.addObject("code", code);
    return mav;
  }
}
