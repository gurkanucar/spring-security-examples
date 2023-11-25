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
    RoleDto adminRole = new RoleDto();
    adminRole.setName("ADMIN");
    adminRole = roleService.createRole(adminRole);

    RoleDto userRole = new RoleDto();
    userRole.setName("USER");
    userRole = roleService.createRole(userRole);

    RoleDto modRole = new RoleDto();
    modRole.setName("MODERATOR");
    modRole = roleService.createRole(modRole);

    UserDto admin = new UserDto();
    admin.setName("admin");
    admin.setUsername("admin");
    admin.setPassword("pass");
    admin.setEnabled(true);
    admin.setCredentialsNonExpired(true);
    admin.setAccountNonExpired(true);
    admin.setAccountNonLocked(true);
    admin.setRoles(Set.of(adminRole, modRole));
    userService.createUser(admin);

    UserDto user = new UserDto();
    user.setName("user");
    user.setUsername("user");
    user.setPassword("pass");
    user.setEnabled(true);
    user.setCredentialsNonExpired(true);
    user.setAccountNonExpired(true);
    user.setAccountNonLocked(true);
    user.setRoles(Set.of(userRole));
    userService.createUser(user);

    UserDto user2 = new UserDto();
    user2.setName("user2");
    user2.setUsername("user2");
    user2.setPassword("pass");
    user2.setRoles(Set.of(userRole));
    userService.createUser(user2);

    UserDto mod = new UserDto();
    mod.setName("mod");
    mod.setUsername("mod");
    mod.setPassword("pass");
    mod.setEnabled(true);
    mod.setCredentialsNonExpired(true);
    mod.setAccountNonExpired(true);
    mod.setAccountNonLocked(true);
    mod.setRoles(Set.of(modRole));
    userService.createUser(mod);
  }
}
