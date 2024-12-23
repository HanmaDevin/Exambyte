package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organizer/tests")
@Secured("ROLE_ORGANIZER")
public class FreeTextQuestionsController {

    private final TestService testService;
    private final FreeTextQuestionService freeTextQuestionService;

    public FreeTextQuestionsController(TestService testService, FreeTextQuestionService freeTextQuestionService) {
        this.testService = testService;
        this.freeTextQuestionService = freeTextQuestionService;
    }

    @GetMapping("/{id}/ft-question")
    public String showCreateFreeTextQuestionForm(@PathVariable Long id, Model model) {
        model.addAttribute("testId", id);
        model.addAttribute("freeTextQuestionDto", new FreeTextQuestionDto());
        return "ft-question-form";
    }

    @PostMapping("/{id}/ft-question")
    public String addFreeTextQuestion(@PathVariable Long id, @ModelAttribute FreeTextQuestionDto freeTextQuestionDto) {
        freeTextQuestionService.createFreeTextQuestion(freeTextQuestionDto);

        testService.addFreeTextQuestionToTest(id, freeTextQuestionService.findFreeTextQuestionById(id));

        // Redirect to the same page to add more questions, ignore this warning
        return "redirect:/organizer/tests/" + id + "/ft-question";
    }

    @GetMapping("/{id}/questions")
    public String showQuestions(@PathVariable Long id, Model model) {
        model.addAttribute("questions", testService.getAllQuestions(id));
        model.addAttribute("test", testService.findTestById(id));
        return "questions";
    }
}
