package de.propra.exambyte.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AppUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User originalUser = defaultService.loadUser(userRequest);
    Set<GrantedAuthority> authorities = new HashSet<>(originalUser.getAuthorities());
    if ("HanmaDevin".equals(originalUser.getAttribute("login"))) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    if ("IliasAlhandiui".equals(originalUser.getAttribute("login"))) {
      authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    return new DefaultOAuth2User(authorities, originalUser.getAttributes(), "id");
  }
}