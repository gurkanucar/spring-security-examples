package com.gucardev.springsecurityexamples.dto;

import com.gucardev.springsecurityexamples.model.OAuth2Provider;
import com.gucardev.springsecurityexamples.validation.CreateValidationGroup;
import com.gucardev.springsecurityexamples.validation.UpdateValidationGroup;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends BaseDto {

  private String email;

  @Null(groups = {CreateValidationGroup.class})
  @NotNull(groups = {UpdateValidationGroup.class})
  private Long id;

  private String name;

  private OAuth2Provider oAuth2Provider;

  private String username;

  private String password;

  private boolean isEnabled;

  private Set<RoleDto> roles;
}
