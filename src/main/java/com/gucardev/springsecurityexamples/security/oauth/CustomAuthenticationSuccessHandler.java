package com.gucardev.springsecurityexamples.security.oauth;

import com.gucardev.springsecurityexamples.model.CustomUserDetails;
import com.gucardev.springsecurityexamples.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired private TokenService tokenService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    handle(request, response, authentication);
    super.clearAuthenticationAttributes(request);
  }

  @Override
  protected void handle(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    //        String targetUrl = "http://localhost:3000/oauth2/redirect";
    String targetUrl = "http://localhost:8080/oauth2/redirect";
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    String token = tokenService.generateAccessToken(user.getUsername());
    targetUrl =
        UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build()
            .toUriString();
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
