package de.propra.exambyte.controller.student;

import de.propra.exambyte.dto.FreeTextAnswerDto;
import de.propra.exambyte.exception.NoTestActiveException;
import de.propra.exambyte.model.Test;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Controller
//@Secured("ROLE_STUDENT")
@RequestMapping("/student")
public class StudentController {

    private final TestService testService;

    public StudentController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/tests")
    public String showDashboard(Model model) {
        model.addAttribute("tests", testService.getAllTests());
        return "student/student-dashboard";
    }

    @GetMapping("/test/{id}")
    public String showTest(Model model, @PathVariable Long id) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        Test test = testService.findTestById(id);

        //if (testService.isActive(id, now)) {
            model.addAttribute("test", test);
            model.addAttribute("test_id", id);
            return "student/test-info";
        //} else {
          //  throw new NoTestActiveException("Test ist nicht aktiv, bitte warten Sie auf den Starttermin: " + test.getStartTime().format(formatter));
       // }
    }

    @GetMapping("/test/{id}/edit/")
    public String editTest(Model model, Principal principal, @PathVariable Long id) {
        Test test = testService.findTestById(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            // Access GitHub-Handle
            String githubHandle = oauthToken.getPrincipal().getAttribute("login");
            testService.setGithubHandle(test, githubHandle);
        }


        model.addAttribute("test", test);
        model.addAttribute("readOnly", testService.hasEnded(id, LocalDateTime.now()));


        return "student/test-edit-form";
    }

    @ExceptionHandler({NoTestActiveException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/student/tests";
    }


}
