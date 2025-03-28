package de.propra.exambyte.controller.student;

import de.propra.exambyte.model.FreeTextAnswer;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.model.Test;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@Secured("ROLE_STUDENT")
@RequestMapping("student/test/{id}/edit/FreeTextQuestion/{id_question}")
public class FreeTextQuestionAnswerController {

    private final FreeTextQuestionService freeTextQuestionService;
    private final TestService testService;

    public FreeTextQuestionAnswerController(FreeTextQuestionService freeTextQuestionService, TestService testService) {
        this.freeTextQuestionService = freeTextQuestionService;
        this.testService = testService;
    }

    @GetMapping()
    public String showFreeTextQuestionAnswerForm(Model model, @PathVariable Long id, @PathVariable Long id_question) {

        testService.findTestById(id);
        FreeTextQuestion currentQuestion = freeTextQuestionService.findFreeTextQuestionById(id_question);
        FreeTextAnswer currentAnswer = currentQuestion.getFreeTextAnswer();

        model.addAttribute("question", currentQuestion);
        model.addAttribute("test_id", id);
        model.addAttribute("question_id", id_question);

        if (currentAnswer != null) {
            model.addAttribute("answer", currentAnswer.getStudentAnswer());
        } else {
            model.addAttribute("answer", "");
        }

        boolean isReadOnly = testService.hasEnded(id, LocalDateTime.now());
        model.addAttribute("readOnly", isReadOnly);
        if (isReadOnly) {
            model.addAttribute("solution", currentQuestion.getPossibleAnswer());
        }


        return "student/free-text-question-answer-form";
    }

    @PostMapping("/submit")
    public String submitAnswer(
            @PathVariable Long id_question,
            @PathVariable Long id,
            @RequestParam("answer") String studentAnswer, RedirectAttributes redirectAttributes) {


        FreeTextQuestion currentQuestion = freeTextQuestionService.findFreeTextQuestionById(id_question);
        freeTextQuestionService.saveOrUpdateStudentAnswer(id_question, studentAnswer);

        Test test = testService.findTestById(id);
        testService.setTestWorkedOnToTrue(test);

        redirectAttributes.addFlashAttribute("savedQuestion", currentQuestion);

        return "redirect:/student/test/{id}/edit/";
    }
}
