package com.gucardev.springsecurityexamples.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ADMIN,
  USER,
  MOD;

  @Override
  public String getAuthority() {
    return name();
  }
}
