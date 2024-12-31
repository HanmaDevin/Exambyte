package de.propra.exambyte.config;

import de.propra.exambyte.service.RoleAssignmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final RoleAssignmentService roleAssignmentService;

    public SecurityConfig(RoleAssignmentService roleAssignmentService) {
        this.roleAssignmentService = roleAssignmentService;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity chainBuilder) throws Exception {
        chainBuilder.authorizeHttpRequests(
                        configurer -> configurer.requestMatchers("/", "/login", "/register", "/css/*").permitAll()
                                .requestMatchers("/organizer/**").hasAuthority("ROLE_ORGANIZER")
                                .requestMatchers("/corrector/**").hasAuthority("ROLE_CORRECTOR")
                                .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService()) // Use a custom user service
                        )
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            System.out.println("Access denied: " + request.getRequestURI());
                            request.setAttribute("error", accessDeniedException.getMessage());
                            request.getRequestDispatcher("/forbidden-access").forward(request, response);
                        })
                );


        return chainBuilder.build();
    }


    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        return new OAuth2UserService<>() {
            private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) {
                OAuth2User oAuth2User = delegate.loadUser(userRequest);
                String githubHandle = oAuth2User.getAttribute("login");
                String role = roleAssignmentService.assignRole(githubHandle);

                // Create the authority list based on the role
                List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + role);

                // Return a new OAuth2User with updated authorities
                return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), "login");
            }
        };
    }
}
