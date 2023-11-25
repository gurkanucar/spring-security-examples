package com.gucardev.springsecurityexamples.dto;

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

  @Null(groups = {CreateValidationGroup.class})
  @NotNull(groups = {UpdateValidationGroup.class})
  private Long id;

  private String name;

  private String username;

  private String email;

  @NotNull(groups = {CreateValidationGroup.class})
  @Null(groups = {UpdateValidationGroup.class})
  private String password;

  private boolean accountNonExpired;

  private boolean isEnabled;

  private boolean accountNonLocked;

  private boolean credentialsNonExpired;

  private Set<RoleDto> roles;
}