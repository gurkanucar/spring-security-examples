package com.gucardev.springsecurityexamples.mapper;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.model.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleDto toDto(Role role);

  Role toEntity(RoleDto role);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePartial(@MappingTarget Role entity, RoleDto dto);
}
