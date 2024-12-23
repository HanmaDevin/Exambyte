package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.model.FreeTextQuestion;
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
public class TestsController {

    private final TestService testService;
    private final FreeTextQuestionService freeTextQuestionService;

    public TestsController(TestService testService, FreeTextQuestionService freeTextQuestionService) {
        this.testService = testService;
        this.freeTextQuestionService = freeTextQuestionService;
    }

    @GetMapping
    public String listTests(Model model) {
        model.addAttribute("tests", testService.getAllTests());
        return "tests";
    }

    @GetMapping("/new")
    public String showCreateTestForm(Model model) {
        model.addAttribute("testDto", new TestDto());
        return "test-form";
    }

    @PostMapping("/new")
    public String createTest(@ModelAttribute TestDto testDto) {
        testService.createTest(testDto);
        return "redirect:/organizer/tests";
    }

    //TODO: Implement DTO for MultipleChoiceQuestion/FreeTextQuestion and Services
//    @GetMapping("/{testId}/mc-questions")
//    public String showCreateMcQuestionForm(@PathVariable Long testId, Model model) {
//        model.addAttribute("testId", testId);
//        model.addAttribute("mcQuestionDto", new MultipleChoiceQuestionDto());
//        return "mc-question-form";
//    }


    @GetMapping("/{id}/ft-question")
    public String showCreateFreeTextQuestionForm(@PathVariable Long id, Model model) {
        model.addAttribute("testId", id);
        model.addAttribute("freeTextQuestionDto", new FreeTextQuestionDto());
        return "free-text-question-form";
    }

    @PostMapping("/{id}/ft-question")
    public String addFreeTextQuestion(@PathVariable Long id, @ModelAttribute FreeTextQuestionDto freeTextQuestionDto, RedirectAttributes redirectAttributes) {
        FreeTextQuestion freeTextQuestion = freeTextQuestionService.createFreeTextQuestion(id, freeTextQuestionDto);
        testService.addFreeTextQuestionToTest(id, freeTextQuestion);
        redirectAttributes.addFlashAttribute("id", id);

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
