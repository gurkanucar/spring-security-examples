package com.gucardev.springsecurityexamples.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.*;

@Data
@Entity(name = "OTP")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OTP extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String username;

  @Column(nullable = false, unique = true, length = 10)
  private String code;

  @Column private Instant expiryDate;

  private OTPType otpType;
}
