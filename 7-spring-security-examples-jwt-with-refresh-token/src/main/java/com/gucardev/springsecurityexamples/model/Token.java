package com.gucardev.springsecurityexamples.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;

@Data
@Entity(name = "TOKEN")
public class Token {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;

  @Column(nullable = false, unique = true, length = 2000)
  private String token;

  @Column private Instant expiryDate;

  private boolean valid;
}
