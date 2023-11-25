package com.gucardev.springsecurityexamples.security.oauth;

import com.gucardev.springsecurityexamples.model.CustomUserDetails;
import com.gucardev.springsecurityexamples.model.OAuth2Provider;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GitHubOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

  @Override
  public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
    CustomUserDetails customUserDetails = new CustomUserDetails();
    customUserDetails.setUsername(retrieveAttr("login", oAuth2User));
    customUserDetails.setName(retrieveAttr("name", oAuth2User));
    customUserDetails.setEmail(retrieveAttr("email", oAuth2User));
    customUserDetails.setAvatarUrl(retrieveAttr("avatar_url", oAuth2User));
    customUserDetails.setProvider(OAuth2Provider.GITHUB);
    customUserDetails.setAttributes(oAuth2User.getAttributes());
    return customUserDetails;
  }

  @Override
  public boolean accepts(OAuth2UserRequest userRequest) {
    return OAuth2Provider.GITHUB
        .name()
        .equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
  }

  private String retrieveAttr(String attr, OAuth2User oAuth2User) {
    Object attribute = oAuth2User.getAttributes().get(attr);
    return attribute == null ? "" : attribute.toString();
  }
}