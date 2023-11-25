package com.gucardev.springsecurityexamples.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestrictedEndpoint {

  private Long roleId;
  private String endpoint;
}
