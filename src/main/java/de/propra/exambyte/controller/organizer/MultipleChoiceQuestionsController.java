package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/organizer/tests")
@Secured("ROLE_ORGANIZER")
public class MultipleChoiceQuestionsController {
    private final MultipleChoiceQuestionService multipleChoiceQuestionService;
    private final TestService testService;

    public MultipleChoiceQuestionsController(MultipleChoiceQuestionService multipleChoiceQuestionService, TestService testService) {
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
                                            @RequestParam List<String> answerTexts,
                                            @RequestParam List<String> answerBooleans,
                                            @ModelAttribute MultipleChoiceQuestionDto dto, RedirectAttributes redirectAttributes) {
        Map<String, Boolean> parsedAnswers = IntStream.range(0, answerTexts.size())
                .boxed()
                .collect(Collectors.toMap(answerTexts::get, i -> Boolean.parseBoolean(answerBooleans.get(i))));
        dto.setAnswers(parsedAnswers);

        MultipleChoiceQuestion createdQuestion = multipleChoiceQuestionService.createMultipleChoiceQuestion(dto);
        testService.addMultipleChoiceQuestionToTest(id, createdQuestion);
        redirectAttributes.addFlashAttribute("createdQuestion", createdQuestion);
        System.out.println(createdQuestion.toString());

        return String.format("redirect:/organizer/tests/%d/mc-question", id);
    }


    /*
     * TODO: handle exception per controller
     * because the global controller advice has conflict with same exception
     * therefore displaying wrong form depending on the question type
     */
    @ExceptionHandler(LowerOrEqualZeroException.class)
    public String handleLowerThanZeroException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("multipleChoiceQuestionDto", new MultipleChoiceQuestionDto());
        return "mc-question-form";
    }

}
