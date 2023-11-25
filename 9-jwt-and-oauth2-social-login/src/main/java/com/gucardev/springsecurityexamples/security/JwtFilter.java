package com.gucardev.springsecurityexamples.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gucardev.springsecurityexamples.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
  private final TokenService tokenService;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final String jwt;
    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }
    jwt = header.substring(7);

    if (tokenService.findInvalidatedTokenByValue(jwt).isPresent()) {
      sendError(response, new Exception("token is not valid!"));
      return;
    }

    DecodedJWT decodedJWT;
    String username;
    try {
      decodedJWT = tokenService.verifyJWT(jwt);
      username = decodedJWT.getSubject();
    } catch (Exception e) {
      sendError(response, new Exception("token is not valid!"));
      return;
    }

    if (username == null) {
      filterChain.doFilter(request, response);
      return;
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    filterChain.doFilter(request, response);
  }

  private void sendError(HttpServletResponse res, Exception e) throws IOException {
    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    res.setContentType("application/json");
    PrintWriter out = res.getWriter();
    ObjectMapper mapper = new ObjectMapper();
    out.println(mapper.writeValueAsString(Map.of("error", e.getMessage(), "code", "-1")));
    out.flush();
  }
}
