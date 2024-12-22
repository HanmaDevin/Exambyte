package de.propra.exambyte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/forbidden-access")
    public String forbiddenAccess() {
        return "error/forbidden-access";
    }
}
