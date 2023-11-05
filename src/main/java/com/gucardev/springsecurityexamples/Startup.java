package com.gucardev.springsecurityexamples;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

  private final UserService userService;

  @Override
  public void run(String... args) {
    createDummyData();
  }

  private void createDummyData() {

    UserDto userDto1 = new UserDto();
    userDto1.setName("user1");
    userDto1.setUsername("user1");
    userDto1.setPassword("pass");
    userDto1.setAuthorities(Set.of(Role.ADMIN, Role.MOD));
    userService.createUser(userDto1);

    UserDto userDto2 = new UserDto();
    userDto2.setName("user2");
    userDto2.setUsername("user2");
    userDto2.setPassword("pass");
    userDto2.setAuthorities(Set.of(Role.MOD));
    userService.createUser(userDto2);

    UserDto userDto3 = new UserDto();
    userDto3.setName("user3");
    userDto3.setUsername("user3");
    userDto3.setPassword("pass");
    userDto3.setAuthorities(Set.of(Role.USER));
    userService.createUser(userDto3);
  }
}
