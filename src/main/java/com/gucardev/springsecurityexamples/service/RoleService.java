package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.mapper.RoleMapper;
import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService {

  private final RoleRepository repository;
  private final RoleMapper mapper;

  public List<RoleDto> getRoles() {
    return repository.findAll().stream().map(mapper::toDto).toList();
  }

  public Optional<Role> getById(Long id) {
    return repository.findById(id);
  }

  public RoleDto createRole(RoleDto dto) {
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public RoleDto updateRole(RoleDto dto) {
    var existing = getById(dto.getId());
    if (existing.isEmpty()) {
      throw new EntityNotFoundException();
    }
    mapper.updatePartial(existing.get(), dto);
    return mapper.toDto(repository.save(existing.get()));
  }
}
