package com.gucardev.springsecurityexamples.repository;

import com.gucardev.springsecurityexamples.model.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

  List<Role> findByUsers_Username(String username);
}
