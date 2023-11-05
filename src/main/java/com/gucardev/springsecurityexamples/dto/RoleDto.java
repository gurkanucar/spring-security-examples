package com.gucardev.springsecurityexamples.dto;

import com.gucardev.springsecurityexamples.validation.CreateValidationGroup;
import com.gucardev.springsecurityexamples.validation.UpdateValidationGroup;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto extends BaseDto {

  @Null(groups = {CreateValidationGroup.class})
  @NotNull(groups = {UpdateValidationGroup.class})
  private Long id;

  private String authority;
}
