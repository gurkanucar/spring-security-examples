package com.gucardev.springsecurityexamples.controller;

import com.gucardev.springsecurityexamples.dto.*;
import com.gucardev.springsecurityexamples.service.AuthService;
import com.gucardev.springsecurityexamples.validation.CreateValidationGroup;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
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

  @RequestMapping("/activate-account")
  public ModelAndView activateAccount(
      @RequestParam(name = "code", required = false) String code,
      @RequestParam(name = "username", required = false) String username,
      Model model) {
    ModelAndView mav;
    try {
      mav = new ModelAndView("activationSuccess.html");
      authService.activateAccount(username, code);
      return mav;
    } catch (Exception e) {
      mav = new ModelAndView("activationError");
      model.addAttribute("error", "Error activating the account: " + e.getMessage());
      return mav;
    }
  }
}
