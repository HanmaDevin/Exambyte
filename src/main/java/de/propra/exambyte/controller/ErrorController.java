package de.propra.exambyte.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/forbidden-access")
    public String forbiddenAccess() {
        return "error/forbidden-access";
    }

    @GetMapping("/test-not-found")
    public String testNotFound() {
        return "error/test-not-found";
    }

    @GetMapping("/free-text-question-not-found")
    public String freeTextQuestionNotFound() {
        return "error/free-text-question-not-found";
    }

    @GetMapping("/multiple-choice-question-not-found")
    public String multipleChoiceQuestionNotFound() {
        return "error/multiple-choice-question-not-found";
    }
}
