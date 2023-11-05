package com.gucardev.springsecurityexamples.controller;

import com.gucardev.springsecurityexamples.dto.RestrictedEndpoint;
import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.service.RoleService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
public class RoleController {

  private final RoleService roleService;

  public RoleController(RoleService roleService) {
    this.roleService = roleService;
  }

  @GetMapping
  public List<RoleDto> getRoles() {
    return roleService.getRoles();
  }

  @PostMapping
  public RoleDto createRole(@RequestBody RoleDto userDto) {
    return roleService.createRole(userDto);
  }

  @PostMapping("/restricted-endpoints")
  public RoleDto addRestrictedEndpoint(@RequestBody RestrictedEndpoint restrictedEndpoint) {
    return roleService.addRestrictedEndpoint(restrictedEndpoint);
  }

  @DeleteMapping("/restricted-endpoints")
  public RoleDto deleteRestrictedEndpoint(@RequestBody RestrictedEndpoint restrictedEndpoint) {
    return roleService.deleteRestrictedEndpoint(restrictedEndpoint);
  }
}
