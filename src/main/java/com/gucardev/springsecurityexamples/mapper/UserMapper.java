package com.gucardev.springsecurityexamples.mapper;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.model.User;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User entity);

  default Role map(GrantedAuthority authority) {
    return Role.valueOf(authority.getAuthority());
  }

  @Mapping(target = "enabled", source = "isEnabled", defaultValue = "false")
  @Mapping(target = "accountNonExpired", defaultValue = "true")
  @Mapping(target = "accountNonLocked", defaultValue = "true")
  @Mapping(target = "credentialsNonExpired", defaultValue = "true")
  User toEntity(UserDto entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePartial(@MappingTarget User entity, UserDto dto);
}
