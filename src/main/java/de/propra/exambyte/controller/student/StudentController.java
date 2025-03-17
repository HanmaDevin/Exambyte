package de.propra.exambyte.controller.student;

import de.propra.exambyte.dto.FreeTextAnswerDto;
import de.propra.exambyte.model.FreeTextAnswer;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.service.FreeTextAnswerService;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Secured("ROLE_STUDENT")
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
        if(testService.isActive(id)){
            model.addAttribute("test", testService.findTestById(id));
            return "student/test-info";
        }
        throw new RuntimeException("Test is not active");
    }

    @GetMapping("/test/{id}/edit/{id_question}")
    public String editTest(Model model, @PathVariable Long id, @PathVariable Long id_question) {
        model.addAttribute("test", testService.findTestById(id));
        model.addAttribute("questionID", testService.getQuestionById(id, id_question));
        model.addAttribute("freeTextAnswerDto", new FreeTextAnswerDto());

        return "student/test-edit-form";
    }

    @PostMapping("/test/{id}/edit/{id_question}")
    public String editTest(@PathVariable Long id, @PathVariable Long id_question, @ModelAttribute FreeTextAnswerDto freeTextAnswerDto, RedirectAttributes redirectAttributes) {
        FreeTextAnswer createedFreeTextAnswer = freeTextAnswerService.createFreeTextAnswer(freeTextAnswerDto);
        FreeTextQuestion freeTextQuestion = freeTextQuestionService.findFreeTextQuestionById(id_question);
        freeTextQuestion.setFreeTextAnswer(createedFreeTextAnswer);

        return String.format("redirect:/student/test/%d/edit/%d", id, id_question);
    }



}
