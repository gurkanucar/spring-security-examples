package com.gucardev.springsecurityexamples.event;

import com.gucardev.springsecurityexamples.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class UserRegisterEvent extends ApplicationEvent {
  private UserDto user;

  public UserRegisterEvent(Object source, UserDto user) {
    super(source);
    this.user = user;
  }
}
