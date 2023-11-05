package com.gucardev.springsecurityexamples.mapper;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.model.User;
import org.mapstruct.*;
import org.springframework.security.core.GrantedAuthority;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User entity);

  User toEntity(UserDto entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePartial(@MappingTarget User entity, UserDto dto);
}
