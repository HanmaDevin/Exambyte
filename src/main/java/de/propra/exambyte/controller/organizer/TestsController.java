package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/organizer/tests")
public class TestsController {

    private final TestService testService;

    public TestsController(TestService testService) {
        this.testService = testService;
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

}
