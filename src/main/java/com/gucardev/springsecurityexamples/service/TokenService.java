package com.gucardev.springsecurityexamples.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gucardev.springsecurityexamples.dto.TokenDto;
import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.Token;
import com.gucardev.springsecurityexamples.repository.TokenRepository;
import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  @Value("${jwt-variables.KEY}")
  private String jwtKey;

  @Value("${jwt-variables.ISSUER}")
  private String jwtIssuer;

  @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
  private long accessTokenExpiryDuration;

  @Value("${jwt-variables.EXPIRES_REFRESH_TOKEN_MINUTE}")
  private long refreshTokenExpiryDuration;

  private final TokenRepository tokenRepository;
  private final UserService userService;

  public TokenDto generateTokenPairs(String username) {
    var user = userService.getDtoByUsername(username);
    return getTokenDto(username, user);
  }

  public TokenDto generateTokenPairsViaRefreshToken(String refreshTokenValue) {
    var existingRefreshToken = tokenRepository.findTokenByTokenAndValidTrue(refreshTokenValue);
    if (existingRefreshToken.isEmpty()) {
      throw new EntityNotFoundException("refresh token not found!");
    }
    verifyRefreshToken(existingRefreshToken.get());
    tokenRepository.delete(existingRefreshToken.get());

    var user = userService.getDtoByUsername(existingRefreshToken.get().getUsername());
    return getTokenDto(user.getUsername(), user);
  }

  private TokenDto getTokenDto(String user, UserDto user1) {
    var accessToken = generateAccessToken(user);
    var refreshToken = generateRefreshToken(user);
    return TokenDto.builder()
        .refreshToken(refreshToken)
        .accessToken(accessToken)
        .user(user1)
        .build();
  }

  public String generateAccessToken(String username) {
    return JWT.create()
        .withSubject(username)
        .withExpiresAt(
            new Date(
                System.currentTimeMillis()
                    + Duration.ofMinutes(accessTokenExpiryDuration).toMillis()))
        .withIssuer(jwtIssuer)
        .sign(Algorithm.HMAC256(jwtKey.getBytes()));
  }

  public DecodedJWT verifyJWT(String token) {
    Algorithm algorithm = Algorithm.HMAC256(jwtKey.getBytes(StandardCharsets.UTF_8));
    JWTVerifier verifier = JWT.require(algorithm).build();
    try {
      return verifier.verify(token);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public String generateRefreshToken(String username) {
    Token refreshToken = new Token();
    refreshToken.setUsername(username);
    refreshToken.setValid(true);
    refreshToken.setExpiryDate(
        Instant.ofEpochSecond(
            System.currentTimeMillis()
                + Duration.ofMinutes(refreshTokenExpiryDuration).toMillis()));
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = tokenRepository.save(refreshToken);
    return refreshToken.getToken();
  }

  public void verifyRefreshToken(Token token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      throw new RuntimeException("token could not verify!");
    }
  }
}
