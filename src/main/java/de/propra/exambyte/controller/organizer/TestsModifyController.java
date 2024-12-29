package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.*;
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
public class TestsModifyController {

    private final TestService testService;

    public TestsModifyController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/{id}/edit")
    public String modifyTest(@PathVariable Long id, Model model) {
        Test test = testService.findTestById(id);
        TestDto currentTest = new TestDto(test.getTitle(), test.getStartTime(), test.getEndTime(), test.getResultTime());
        System.out.println(currentTest);
        model.addAttribute("currentTest", currentTest);
        model.addAttribute("testId", id);
        return "test-form-modify";
    }

    @PostMapping("/{id}/edit")
    public String saveTest(@PathVariable Long id, @ModelAttribute TestDto modifiedTest, RedirectAttributes redirectAttributes) {
        Test updatedTest = testService.updateTest(id, modifiedTest);
        redirectAttributes.addFlashAttribute("updatedTest", updatedTest);
        return "redirect:/organizer/tests";
    }

    @ExceptionHandler(TestNotFoundException.class)
    public String handleTestNotFoundException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/test-not-found";
    }

    @ExceptionHandler({WrongDateInputException.class, EmptyInputException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/organizer/tests/{id}/edit";
    }
}
