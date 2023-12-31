package com.gucardev.springsecurityexamples.controller;

import com.gucardev.springsecurityexamples.dto.LoginRequest;
import com.gucardev.springsecurityexamples.dto.RefreshTokenRequest;
import com.gucardev.springsecurityexamples.dto.TokenDto;
import com.gucardev.springsecurityexamples.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginRequest loginRequest) {
    return ResponseEntity.ok().body(authService.login(loginRequest));
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<TokenDto> refreshToken(
      @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ResponseEntity.ok().body(authService.refreshToken(refreshTokenRequest));
  }
}
