package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

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
    public String addMultipleChoiceQuestion(@PathVariable Long id, @ModelAttribute MultipleChoiceQuestionDto multipleChoiceQuestionDto) {
        MultipleChoiceQuestion createdQuestion =  multipleChoiceQuestionService.createMultipleChoiceQuestion(multipleChoiceQuestionDto);
        testService.addMultipleChoiceQuestionToTest(id, createdQuestion);
        return String.format("redirect:/organizer/tests/%d/mc-question", id);
    }


}
