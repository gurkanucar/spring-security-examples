package com.gucardev.springsecurityexamples.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(name = "account_non_expired", columnDefinition = "boolean default true")
  private boolean accountNonExpired;

  @Column(name = "is_enabled", columnDefinition = "boolean default true")
  private boolean isEnabled;

  @Column(name = "account_non_locked", columnDefinition = "boolean default true")
  private boolean accountNonLocked;

  @Column(name = "credentials_non_expired", columnDefinition = "boolean default true")
  private boolean credentialsNonExpired;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role authority;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(this.authority);
  }
}
