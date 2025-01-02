package de.propra.exambyte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collection;

@Controller
public class WebController {
    @GetMapping("/")
    public String home(Model model, Principal principal) {

        // Redirect unauthorized user and extract roles
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ANONYMOUS"))) {
            return "redirect:/oauth2/authorization/github";
        }

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            // Access GitHub-Handle
            String githubHandle = oauthToken.getPrincipal().getAttribute("login");
            model.addAttribute("githubHandle", githubHandle);
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        model.addAttribute("authorities", authorities);

        //only for debugging purposes
        System.out.println("User: " + principal.getName() + " mit der Rolle " + authorities.toString());
        return "public/home";
    }

    // Dummy method to throw an exception
    @GetMapping("/dummyError")
    public String dummyError() throws Exception {
        throw new AccessDeniedException("Access Denied");
    }
}

