package de.propra.exambyte.controller;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

// This class is only for logik testing, could change a lot

@Controller
@RequestMapping("/tests")
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
        model.addAttribute("testDto", new TestDto(
                "",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusHours(2)
        ));
        return "test-form";
    }

    @PostMapping("/new")
    public String createTest(@ModelAttribute TestDto testDto) {
        testService.createTest(testDto);
        return "redirect:/tests";
    }

}
