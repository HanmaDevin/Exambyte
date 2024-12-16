package de.propra.exambyte;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/register")
    public String register() {
        return "public/register";
    }
}
