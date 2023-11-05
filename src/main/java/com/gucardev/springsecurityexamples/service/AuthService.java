package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.dto.LoginRequest;
import com.gucardev.springsecurityexamples.dto.TokenDto;
import com.gucardev.springsecurityexamples.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

  private final UserMapper userMapper;
  private final TokenService tokenService;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;

  public TokenDto login(LoginRequest loginRequest) {
    try {
      var user =
          userService
              .getByUsername(loginRequest.getUsername())
              .orElseThrow(() -> new EntityNotFoundException("user not found!"));
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginRequest.getUsername(), loginRequest.getPassword()));
      return TokenDto.builder()
          .accessToken(tokenService.generateToken(loginRequest.getUsername()))
          .user(userMapper.toDto(user))
          .build();
    } catch (Exception e) {
      throw new RuntimeException();
    }
  }
}
