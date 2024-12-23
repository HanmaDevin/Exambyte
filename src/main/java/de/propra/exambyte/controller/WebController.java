package de.propra.exambyte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;

@Controller
public class WebController {
    @GetMapping("/")
    public String home(OAuth2AuthenticationToken oauth, Model model) {
        if (oauth != null) {
            String clientId = oauth.getAuthorizedClientRegistrationId();
            model.addAttribute("id", clientId);
        }
        return "public/home";
    }

    // Temp dashboard for debugging
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extract roles
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        model.addAttribute("roles", authorities);

        return "public/organizer";
    }
}

