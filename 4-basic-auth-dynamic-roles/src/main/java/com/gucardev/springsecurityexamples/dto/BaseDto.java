package com.gucardev.springsecurityexamples.dto;

import jakarta.validation.constraints.Null;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseDto {

  // these are must be null in requests and set in backend
  @Null private LocalDateTime createdDateTime;

  @Null private LocalDateTime updatedDateTime;

  @Null private String createdBy;

  @Null private String updatedBy;
}
