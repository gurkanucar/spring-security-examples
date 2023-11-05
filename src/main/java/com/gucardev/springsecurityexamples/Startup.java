package com.gucardev.springsecurityexamples;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.service.RoleService;
import com.gucardev.springsecurityexamples.service.UserService;
import com.gucardev.springsecurityexamples.util.JsonProcessorUtil;
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
    jsonProcessorUtil.processJson(
        "data/Roles.json", RoleDto.class, (x) -> roleService.createRole(x));
    jsonProcessorUtil.processJson(
        "data/Users.json", UserDto.class, (x) -> userService.createUser(x));
  }
}
