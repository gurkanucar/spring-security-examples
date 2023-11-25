package com.gucardev.springsecurityexamples.security.oauth;

import com.gucardev.springsecurityexamples.dto.RoleDto;
import com.gucardev.springsecurityexamples.dto.UserDto;
import com.gucardev.springsecurityexamples.model.CustomUserDetails;
import com.gucardev.springsecurityexamples.service.RoleService;
import com.gucardev.springsecurityexamples.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  private final UserService userService;
  private final RoleService roleService;
  private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional =
        oAuth2UserInfoExtractors.stream()
            .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
            .findFirst();
    if (oAuth2UserInfoExtractorOptional.isEmpty()) {
      throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
    }

    CustomUserDetails customUserDetails =
        oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
    UserDto user = upsertUser(customUserDetails);
    customUserDetails.setId(user.getId());
    return customUserDetails;
  }

  private UserDto upsertUser(CustomUserDetails customUserDetails) {
    UserDto existingUser;
    try {
      existingUser = userService.getDtoByUsername(customUserDetails.getUsername());
    } catch (Exception e) {

      RoleDto role =
          roleService.getRoles().stream().filter(x -> x.getName().equals("USER")).findFirst().get();

      UserDto user = new UserDto();
      user.setName(customUserDetails.getName());
      user.setUsername(customUserDetails.getUsername());
      user.setEmail(customUserDetails.getEmail());
      user.setOAuth2Provider(customUserDetails.getProvider());
      user.setEnabled(true);
      user.setCredentialsNonExpired(true);
      user.setAccountNonExpired(true);
      user.setAccountNonLocked(true);
      user.setRoles(Set.of(role));
      return userService.createUser(user);
    }
    //    update existing
    //    existingUser.setEmail(customUserDetails.getEmail());
    //    existingUser.setImageUrl(customUserDetails.getAvatarUrl());

    return existingUser;
  }
}
