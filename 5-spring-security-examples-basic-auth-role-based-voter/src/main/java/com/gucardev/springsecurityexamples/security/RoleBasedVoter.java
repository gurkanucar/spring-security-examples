package com.gucardev.springsecurityexamples.security;

import com.gucardev.springsecurityexamples.model.Role;
import com.gucardev.springsecurityexamples.repository.RoleRepository;
import java.util.List;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleBasedVoter implements AuthorizationManager<RequestAuthorizationContext> {

  private final RoleRepository roleRepository;

  @Override
  public AuthorizationDecision check(
      Supplier<Authentication> authentication, RequestAuthorizationContext object) {
    if (authentication.get().getPrincipal() instanceof UserDetails) {
      UserDetails userDetails = (UserDetails) authentication.get().getPrincipal();
      String requestUrl = object.getRequest().getRequestURI();

      List<Role> roles = roleRepository.findByUsers_Username(userDetails.getUsername());

      for (Role role : roles) {
        if (role.getRestrictedEndpoints().contains(requestUrl)) {
          return new AuthorizationDecision(false);
        }
      }
    }

    return new AuthorizationDecision(true);
  }
}
