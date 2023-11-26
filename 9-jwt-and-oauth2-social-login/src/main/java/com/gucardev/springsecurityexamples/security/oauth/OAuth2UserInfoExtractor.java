package com.gucardev.springsecurityexamples.security.oauth;

import com.gucardev.springsecurityexamples.security.CustomOauthUserDetails;
import com.gucardev.springsecurityexamples.security.CustomUserDetails;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuth2UserInfoExtractor {

    CustomOauthUserDetails extractUserInfo(OAuth2User oAuth2User);

    boolean accepts(OAuth2UserRequest userRequest);
}
