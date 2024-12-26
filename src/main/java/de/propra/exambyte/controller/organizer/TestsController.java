package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.Questions;
import de.propra.exambyte.model.Test;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


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
    public String createTest(@ModelAttribute TestDto testDto, Model model) {
        Test createdTest = testService.createTest(testDto);
        System.out.println(createdTest.toString());
        model.addAttribute("createdTest", createdTest);
        return "test-form";
    }


    //This is just for testing purposes and should be removed later
    private static int testCounter = 1;
    @PostMapping("/new/fill")
    public String createFilledTest(@ModelAttribute TestDto testDto, RedirectAttributes redirectAttributes) {
        testDto.fillTestValues();
        testDto.setTitle("Test " + testCounter);
        testCounter++;
        Test createdTest = testService.createTest(testDto);
        redirectAttributes.addFlashAttribute("createdTest", createdTest);
        System.out.println(createdTest.toString());
        return "redirect:/organizer/tests";
    }



    @GetMapping("/{id}/questions")
    public String showQuestions(@PathVariable Long id, Model model) {
        List<Questions> allQuestions = testService.getAllQuestions(id);
        model.addAttribute("questions", allQuestions);
        model.addAttribute("test", testService.findTestById(id));
        return "questions";
    }

    @ExceptionHandler(TestNotFoundException.class)
    public String handleTestNotFoundException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/test-not-found";
    }

    @ExceptionHandler(WrongDateInputException.class)
    public String handleWrongDateInput(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("testDto", new TestDto());
        return "test-form";
    }

}
