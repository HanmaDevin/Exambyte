package de.propra.exambyte.controller.student;

import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Secured("ROLE_STUDENT")
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
        model.addAttribute("test", testService.findTestById(id));
        return "student/test-info";
    }

    @GetMapping("/test/{id}/edit")
    public String editTest(Model model, @PathVariable Long id) {
        model.addAttribute("test", testService.findTestById(id));
        return "student/test-edit";
    }
}
