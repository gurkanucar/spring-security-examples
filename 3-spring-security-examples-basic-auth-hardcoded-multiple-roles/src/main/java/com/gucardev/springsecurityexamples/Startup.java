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

    UserDto admin = new UserDto();
    admin.setName("admin");
    admin.setUsername("admin");
    admin.setPassword("pass");
    admin.setEnabled(true);
    admin.setRoles(Set.of(Role.ROLE_ADMIN, Role.ROLE_MOD));
    userService.createUser(admin);

    UserDto user = new UserDto();
    user.setName("user");
    user.setUsername("user");
    user.setPassword("pass");
    user.setEnabled(true);
    user.setRoles(Set.of(Role.ROLE_USER));
    userService.createUser(user);

    UserDto user2 = new UserDto();
    user2.setName("user2");
    user2.setUsername("user2");
    user2.setPassword("pass");
    user2.setRoles(Set.of(Role.ROLE_USER));
    userService.createUser(user2);

    UserDto mod = new UserDto();
    mod.setName("mod");
    mod.setUsername("mod");
    mod.setPassword("pass");
    mod.setEnabled(true);
    mod.setRoles(Set.of(Role.ROLE_MOD));
    userService.createUser(mod);
  }
}
