package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.dto.LoginRequest;
import com.gucardev.springsecurityexamples.dto.RefreshTokenRequest;
import com.gucardev.springsecurityexamples.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

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
}
