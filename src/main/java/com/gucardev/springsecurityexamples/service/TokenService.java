package com.gucardev.springsecurityexamples.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gucardev.springsecurityexamples.dto.TokenDto;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
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

  private final UserService userService;

  public TokenDto generateTokenPairs(String username) {
    var user = userService.getDtoByUsername(username);
    var accessToken = generateAccessToken(username);
    return TokenDto.builder().accessToken(accessToken).user(user).build();
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
}
