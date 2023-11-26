package com.gucardev.springsecurityexamples.security;

import com.gucardev.springsecurityexamples.model.OAuth2Provider;
import java.util.Collection;
import java.util.Map;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Data
public class CustomOauthUserDetails implements OAuth2User {
  private Long id;
  private String username;
  private String name;
  private String email;
  private String avatarUrl;
  private OAuth2Provider provider;
  private Collection<? extends GrantedAuthority> authorities;
  private Map<String, Object> attributes;
}
