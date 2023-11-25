package com.gucardev.springsecurityexamples;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.service.UserService;
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
    admin.setCredentialsNonExpired(true);
    admin.setAccountNonExpired(true);
    admin.setAccountNonLocked(true);
    admin.setAuthority(Role.ROLE_ADMIN);
    userService.createUser(admin);

    UserDto user = new UserDto();
    user.setName("user");
    user.setUsername("user");
    user.setPassword("pass");
    user.setEnabled(true);
    user.setCredentialsNonExpired(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setAuthority(Role.ROLE_USER);
    userService.createUser(user);

    UserDto user2 = new UserDto();
    user2.setName("user2");
    user2.setUsername("user2");
    user2.setPassword("pass");
    user2.setAuthority(Role.ROLE_USER);
    userService.createUser(user2);

    UserDto mod = new UserDto();
    mod.setName("mod");
    mod.setUsername("mod");
    mod.setPassword("pass");
    mod.setEnabled(true);
    mod.setCredentialsNonExpired(true);
    mod.setAccountNonExpired(true);
    mod.setAccountNonLocked(true);
    mod.setAuthority(Role.ROLE_MOD);
    userService.createUser(mod);
  }
}
