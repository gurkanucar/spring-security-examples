package com.gucardev.springsecurityexamples.service;

import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.mapper.UserMapper;
import com.gucardev.springsecurityexamples.model.User;
import com.gucardev.springsecurityexamples.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserMapper mapper;

  public List<UserDto> getUsers() {
    return repository.findAll().stream().map(mapper::toDto).toList();
  }

  public Optional<User> getById(Long id) {
    return repository.findById(id);
  }

  public Optional<User> getByUsername(String username) {
    return repository.findByUsernameAndIsEnabledTrue(username);
  }

  public UserDto createUser(UserDto roleDto) {
    return mapper.toDto(repository.save(mapper.toEntity(roleDto)));
  }

  public UserDto updateUser(UserDto roleDto) {
    var existing = getById(roleDto.getId());
    if (existing.isEmpty()) {
      throw new EntityNotFoundException();
    }
    mapper.updatePartial(existing.get(), roleDto);
    return mapper.toDto(repository.save(existing.get()));
  }
}
