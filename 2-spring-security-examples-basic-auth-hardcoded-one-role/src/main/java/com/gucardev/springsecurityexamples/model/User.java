package com.gucardev.springsecurityexamples.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "USERS")
@Getter
@Setter
public class User extends BaseEntity {

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  private boolean isEnabled;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;
}
