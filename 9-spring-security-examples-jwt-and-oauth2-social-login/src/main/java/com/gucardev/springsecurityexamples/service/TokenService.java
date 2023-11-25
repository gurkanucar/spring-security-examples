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
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

  private final TokenRepository tokenRepository;
  private final UserService userService;

  @Value("${jwt-variables.KEY}")
  private String jwtKey;

  @Value("${jwt-variables.ISSUER}")
  private String jwtIssuer;

  @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
  private long accessTokenExpiryDuration;

  @Value("${jwt-variables.EXPIRES_REFRESH_TOKEN_MINUTE}")
  private long refreshTokenExpiryDuration;

  public TokenDto generateTokenPairs(String username) {
    var user = userService.getDtoByUsername(username);
    return getTokenDto(user);
  }

  public TokenDto generateTokenPairsViaRefreshToken(String refreshTokenValue) {
    var existingRefreshToken = tokenRepository.findTokenByTokenAndValidTrue(refreshTokenValue);
    if (existingRefreshToken.isEmpty()) {
      throw new EntityNotFoundException("refresh token not found!");
    }
    verifyRefreshToken(existingRefreshToken.get());
    tokenRepository.delete(existingRefreshToken.get());
    var user = userService.getDtoByUsername(existingRefreshToken.get().getUsername());
    return getTokenDto(user);
  }

  private TokenDto getTokenDto(UserDto user) {
    var accessToken = generateAccessToken(user.getUsername());
    var refreshToken = generateRefreshToken(user.getUsername());
    return TokenDto.builder()
        .refreshToken(refreshToken)
        .accessToken(accessToken)
        .user(user)
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
    Instant expirationTime = Instant.now().plus(Duration.ofMinutes(refreshTokenExpiryDuration));
    Token refreshToken = new Token();
    refreshToken.setUsername(username);
    refreshToken.setValid(true);
    refreshToken.setExpiryDate(expirationTime);
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken = tokenRepository.save(refreshToken);
    return refreshToken.getToken();
  }

  public void verifyRefreshToken(Token token) {
    if (token.getExpiryDate().isBefore(Instant.now())) {
      throw new RuntimeException("Token has expired and cannot be used!");
    }
  }

  public Optional<Token> findInvalidatedTokenByValue(String token) {
    return tokenRepository.findTokenByTokenAndValidFalse(token);
  }

  public void invalidateToken(String tokenValue) {
    var existingToken = tokenRepository.findTokenByTokenAndValidFalse(tokenValue);
    if (existingToken.isPresent()) {
      throw new EntityNotFoundException("token already invalidated!");
    }
    Token token = new Token();
    token.setUsername(JWT.decode(tokenValue).getSubject());
    token.setValid(false);
    token.setToken(tokenValue);
    tokenRepository.save(token);
  }
}
