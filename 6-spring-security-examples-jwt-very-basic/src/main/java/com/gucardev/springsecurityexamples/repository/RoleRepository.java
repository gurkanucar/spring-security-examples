package com.gucardev.springsecurityexamples.repository;

import com.gucardev.springsecurityexamples.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {}
