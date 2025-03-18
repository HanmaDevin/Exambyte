package de.propra.exambyte.controller.student;

import de.propra.exambyte.dto.FreeTextAnswerDto;
import de.propra.exambyte.exception.NoTestActiveException;
import de.propra.exambyte.service.FreeTextAnswerService;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;


@Controller
//@Secured("ROLE_STUDENT")
@RequestMapping("/student")
public class StudentController {

    private final TestService testService;
    private final FreeTextAnswerService freeTextAnswerService;
    private final FreeTextQuestionService freeTextQuestionService;
    private final MultipleChoiceQuestionService multipleChoiceQuestionService;
    // private final MultipleChoiceAnswerService multipleChoiceAnswerService;

    public StudentController(TestService testService, FreeTextAnswerService freeTextAnswerService, FreeTextQuestionService freeTextQuestionService, MultipleChoiceQuestionService multipleChoiceQuestionService) {
        this.multipleChoiceQuestionService = multipleChoiceQuestionService;
        this.freeTextQuestionService = freeTextQuestionService;
        this.freeTextAnswerService = freeTextAnswerService;
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
        if (testService.isActive(id, now)) {
            model.addAttribute("test", testService.findTestById(id));
            model.addAttribute("test_id", id);
            return "student/test-info";
        } else if (testService.isEnded(id, now)) {
            model.addAttribute("test", testService.findTestById(id));
            model.addAttribute("test_id", id);
            return "student/test-info";
        } else {
            throw new NoTestActiveException("Test ist nicht aktiv, bitte warten Sie auf den Starttermin. :" + testService.findTestById(id).getStartTime());
        }
    }

    @GetMapping("/test/{id}/edit/")
    public String editTest(Model model, @PathVariable Long id) {
        model.addAttribute("test", testService.findTestById(id));
        model.addAttribute("freeTextAnswerDto", new FreeTextAnswerDto());

        return "student/test-edit-form";
    }

    @ExceptionHandler({NoTestActiveException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/student/tests";
    }


}
