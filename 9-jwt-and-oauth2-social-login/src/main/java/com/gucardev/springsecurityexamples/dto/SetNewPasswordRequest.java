package com.gucardev.springsecurityexamples.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetNewPasswordRequest {
  private String password;
  private String username;
  private String code;
}
