package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.model.Test;
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
    public String createTest(@ModelAttribute TestDto testDto, RedirectAttributes redirectAttributes) {
        Test createdTest = testService.createTest(testDto);
        redirectAttributes.addFlashAttribute("createdTest", createdTest);
        System.out.println(createdTest.toString());
        return "redirect:/organizer/tests";
    }

    // TODO: Need to display whether the question is a multiple choice or free text question and display the question accordingly
    @GetMapping("/{id}/questions")
    public String showQuestions(@PathVariable Long id, Model model) {
        model.addAttribute("questions", testService.getAllQuestions(id));
        model.addAttribute("test", testService.findTestById(id));
        return "questions";
    }

}
