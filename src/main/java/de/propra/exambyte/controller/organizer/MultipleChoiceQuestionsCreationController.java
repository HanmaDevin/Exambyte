package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.DuplicateAnswerException;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.exception.NoAnswersMarkedCorrectException;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;


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
        model.addAttribute("testId", id);
        model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
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


    @ExceptionHandler(LowerOrEqualZeroException.class)
    public String handleLowerThanZeroException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        return "mc-question-form";
    }

    //TODO: update this Exception
    @ExceptionHandler(DuplicateAnswerException.class)
    public String handleDuplicateAnswerException(DuplicateAnswerException e,
                                                 Model model,
                                                 RedirectAttributes redirectAttributes) {
        Map<String, Object> attributes = model.asMap();
        if (attributes.containsKey("answerTexts") && attributes.containsKey("answerBooleans")) {
            redirectAttributes.addFlashAttribute("answerTexts", attributes.get("answerTexts"));
            redirectAttributes.addFlashAttribute("answerBooleans", attributes.get("answerBooleans"));
            redirectAttributes.addFlashAttribute("multipleChoiceQuestionDto", attributes.get("multipleChoiceQuestionDto"));
        }

        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/organizer/tests/{id}/mc-question";
    }

    @ExceptionHandler(EmptyInputException.class)
    public String handleMultipleChoiceEmptyInputException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        return "mc-question-form";
    }

    @ExceptionHandler(NoAnswersMarkedCorrectException.class)
    public String handleNoAnswersMarkedCorrectException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        return "mc-question-form";
    }
}
