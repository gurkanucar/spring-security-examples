package com.gucardev.springsecurityexamples;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.service.RoleService;
import com.gucardev.springsecurityexamples.service.UserService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

  private final RoleService roleService;
  private final UserService userService;

  @Override
  public void run(String... args) {
    createDummyData();
  }

  private void createDummyData() {
    RoleDto role1 = new RoleDto();
    role1.setAuthority("ADMIN");
    role1 = roleService.createRole(role1);

    RoleDto role2 = new RoleDto();
    role2.setAuthority("USER");
    role2 = roleService.createRole(role2);

    RoleDto role3 = new RoleDto();
    role3.setAuthority("MODERATOR");
    role3 = roleService.createRole(role3);

    UserDto admin = new UserDto();
    admin.setName("admin");
    admin.setUsername("admin");
    admin.setPassword("pass");
    admin.setEnabled(true);
    admin.setCredentialsNonExpired(true);
    admin.setAccountNonExpired(true);
    admin.setAccountNonLocked(true);
    admin.setAuthorities(Set.of(role1, role3));
    userService.createUser(admin);

    UserDto user = new UserDto();
    user.setName("user");
    user.setUsername("user");
    user.setPassword("pass");
    user.setEnabled(true);
    user.setCredentialsNonExpired(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setAuthorities(Set.of(role2));
    userService.createUser(user);

    UserDto user2 = new UserDto();
    user2.setName("user2");
    user2.setUsername("user2");
    user2.setPassword("pass");
    user2.setAuthorities(Set.of(role2));
    userService.createUser(user2);

    UserDto mod = new UserDto();
    mod.setName("mod");
    mod.setUsername("mod");
    mod.setPassword("pass");
    mod.setEnabled(true);
    mod.setCredentialsNonExpired(true);
    mod.setAccountNonExpired(true);
    mod.setAccountNonLocked(true);
    mod.setAuthorities(Set.of(role3));
    userService.createUser(mod);
  }
}
