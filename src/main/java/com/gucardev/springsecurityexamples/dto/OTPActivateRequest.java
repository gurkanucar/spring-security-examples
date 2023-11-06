package com.gucardev.springsecurityexamples.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OTPActivateRequest {

  private String username;
  private String code;
}
