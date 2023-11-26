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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  public List<UserDto> getUsers() {
    return repository.findAll().stream().map(mapper::toDto).toList();
  }

  public Optional<User> getById(Long id) {
    return repository.findById(id);
  }

  public User getByUsername(String username) {
    var user = repository.findByUsernameAndIsEnabledTrue(username);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("user not found!");
    }
    return user.get();
  }

  public UserDto getDtoByUsername(String username) {
    var user = repository.findByUsername(username);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("user not found!");
    }
    return mapper.toDto(user.get());
  }

  public UserDto getDtoByEmail(String email) {
    var user = repository.findByEmail(email);
    if (user.isEmpty()) {
      throw new EntityNotFoundException("user not found!");
    }
    return mapper.toDto(user.get());
  }

  public UserDto createUser(UserDto dto) {
    if (repository.existsByUsername(dto.getUsername())
        || repository.existsByEmail(dto.getEmail())) {
      throw new RuntimeException("user is already exists!");
    }
    if (dto.getPassword() != null) {
      dto.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
    return mapper.toDto(repository.save(mapper.toEntity(dto)));
  }

  public UserDto updateUser(UserDto dto) {
    var existing = getById(dto.getId());
    if (existing.isEmpty()) {
      throw new EntityNotFoundException();
    }
    mapper.updatePartial(existing.get(), dto);
    return mapper.toDto(repository.save(existing.get()));
  }
}
