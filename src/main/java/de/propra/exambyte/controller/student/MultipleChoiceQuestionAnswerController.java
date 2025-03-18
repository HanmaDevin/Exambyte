package de.propra.exambyte.controller.student;

import de.propra.exambyte.model.MultipleChoiceAnswer;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.model.Test;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
//@Secured("ROLE_STUDENT")
@RequestMapping("student/test/{id}/edit/MultipleChoiceQuestion/{id_question}")
public class MultipleChoiceQuestionAnswerController {

    private final MultipleChoiceQuestionService multipleChoiceQuestionService;
    private final TestService testService;

    public MultipleChoiceQuestionAnswerController(MultipleChoiceQuestionService multipleChoiceQuestionService, TestService testService) {
        this.multipleChoiceQuestionService = multipleChoiceQuestionService;
        this.testService = testService;
    }

    @GetMapping()
    public String showMultipleChoiceQuestion(Model model, @PathVariable Long id, @PathVariable Long id_question) {
        testService.findTestById(id);
        MultipleChoiceQuestion currentQuestion = multipleChoiceQuestionService.findMultipleChoiceQuestionById(id_question);
        MultipleChoiceAnswer currentAnswer = currentQuestion.getMultipleChoiceAnswer();

        model.addAttribute("question", currentQuestion);
        model.addAttribute("test_id", id);
        model.addAttribute("question_id", id_question);

        if (currentAnswer != null) {
            model.addAttribute("selectedAnswers", currentAnswer.getSelectedAnswers());
        } else {
            model.addAttribute("selectedAnswers", "");
        }

        return "student/multiple-choice-question-answer-form";
    }

    @PostMapping("/submit")
    public String submitAnswer(
            @PathVariable Long id_question,
            @PathVariable Long id,
            @RequestParam(value = "selectedAnswers", required = false) Set<String> selectedAnswers, RedirectAttributes redirectAttributes) {
        MultipleChoiceQuestion currentQuestion = multipleChoiceQuestionService.findMultipleChoiceQuestionById(id_question);
        multipleChoiceQuestionService.saveOrUpdateStudentAnswer(id_question, selectedAnswers);

        Test test = testService.findTestById(id);
        testService.setTestWorkedOnToTrue(test);

        redirectAttributes.addFlashAttribute("savedQuestion", currentQuestion);

        return "redirect:/student/test/{id}/edit/";
    }
}
