package com.gucardev.springsecurityexamples;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.service.RoleService;
import com.gucardev.springsecurityexamples.service.UserService;
import com.gucardev.springsecurityexamples.util.JsonProcessorUtil;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Startup implements CommandLineRunner {

  private final JsonProcessorUtil jsonProcessorUtil;
  private final RoleService roleService;
  private final UserService userService;

  @Override
  public void run(String... args) throws Exception {
    createDummyData();
  }

  private void createDummyData() throws Exception {
    RoleDto role1 = new RoleDto();
    role1.setAuthority("ADMIN");
    role1 = roleService.createRole(role1);

    RoleDto role2 = new RoleDto();
    role2.setAuthority("USER");
    role2 = roleService.createRole(role2);

    RoleDto role3 = new RoleDto();
    role3.setAuthority("MODERATOR");
    role3 = roleService.createRole(role3);

    UserDto userDto1 = new UserDto();
    userDto1.setName("user1");
    userDto1.setUsername("user1");
    userDto1.setPassword("pass");
    userDto1.setAuthorities(Set.of(role1, role3));
    userService.createUser(userDto1);

    UserDto userDto2 = new UserDto();
    userDto2.setName("user2");
    userDto2.setUsername("user2");
    userDto2.setPassword("pass");
    userDto2.setAuthorities(Set.of(role1, role3));
    userService.createUser(userDto2);

    UserDto userDto3 = new UserDto();
    userDto3.setName("user3");
    userDto3.setUsername("user3");
    userDto3.setPassword("pass");
    userDto3.setAuthorities(Set.of(role2));
    userService.createUser(userDto3);
  }
}
