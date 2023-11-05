package com.gucardev.springsecurityexamples.mapper;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(User entity);

  @Mapping(target = "enabled", source = "isEnabled", defaultValue = "false")
  @Mapping(target = "accountNonExpired", defaultValue = "true")
  @Mapping(target = "accountNonLocked", defaultValue = "true")
  @Mapping(target = "credentialsNonExpired", defaultValue = "true")
  User toEntity(UserDto entity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePartial(@MappingTarget User entity, UserDto dto);
}
