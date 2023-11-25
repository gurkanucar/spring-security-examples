package com.gucardev.springsecurityexamples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
public class Role extends BaseEntity implements GrantedAuthority {

  @Column(name = "role_name")
  private String name;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "role_endpoints", joinColumns = @JoinColumn(name = "role_id"))
  @Column(name = "restricted_endpoint")
  private Set<String> restrictedEndpoints = new HashSet<>();

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE},
      mappedBy = "roles")
  @JsonIgnore
  private Set<User> users = new HashSet<>();

  @Override
  public String getAuthority() {
    return getName();
  }
}
