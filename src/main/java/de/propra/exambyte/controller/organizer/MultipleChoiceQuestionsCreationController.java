package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.*;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/organizer/tests")
@Secured("ROLE_ORGANIZER")
public class MultipleChoiceQuestionsCreationController {
    private final MultipleChoiceQuestionService multipleChoiceQuestionService;
    private final TestService testService;

    public MultipleChoiceQuestionsCreationController(MultipleChoiceQuestionService multipleChoiceQuestionService, TestService testService) {
        this.multipleChoiceQuestionService = multipleChoiceQuestionService;
        this.testService = testService;
    }

    @GetMapping("/{id}/mc-question")
    public String showCreateMultipleChoiceQuestionForm(@PathVariable Long id, Model model) {
        if (!model.containsAttribute("multipleChoiceQuestionDto")) {
            model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        }
        model.addAttribute("testId", id);
        return "mc-question-form";
    }


    @PostMapping("/{id}/mc-question")
    public String addMultipleChoiceQuestion(@PathVariable Long id,
                                            @ModelAttribute MultipleChoiceQuestionDto dto,
                                            RedirectAttributes redirectAttributes) {
        dto.parseAnswers();
        MultipleChoiceQuestion createdQuestion = multipleChoiceQuestionService.createMultipleChoiceQuestion(dto);
        testService.addMultipleChoiceQuestionToTest(id, createdQuestion);

        redirectAttributes.addFlashAttribute("createdQuestion", createdQuestion);
        System.out.println(createdQuestion.toString());

        return String.format("redirect:/organizer/tests/%d/mc-question", id);
    }


    @ExceptionHandler({LowerOrEqualZeroException.class, DuplicateAnswerException.class, EmptyInputException.class, NoAnswersMarkedCorrectException.class, InsufficientAnswersException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        redirectAttributes.addFlashAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        return "redirect:/organizer/tests/{id}/mc-question";
    }

    @ExceptionHandler(TestNotFoundException.class)
    public String handleTestNotFoundException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/test-not-found";
    }
}
