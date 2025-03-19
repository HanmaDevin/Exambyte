package de.propra.exambyte.controller.corrector;

import de.propra.exambyte.exception.NoTestActiveException;
import de.propra.exambyte.exception.TestHasNotEndedYetException;
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
//@Secured("ROLE_CORRECTOR")
@RequestMapping("/corrector")
public class CorrectorController {

    private final TestService testService;
    private final FreeTextQuestionService freeTextQuestionService;

    public CorrectorController(TestService testService, FreeTextQuestionService freeTextQuestionService) {
        this.testService = testService;
        this.freeTextQuestionService = freeTextQuestionService;
    }

    @GetMapping("/tests")
    public String index(Model model) {
        model.addAttribute("tests", testService.getAllTests());
        return "corrector/corrector";
    }

    @GetMapping("/tests/{id}")
    public String showTest(Model model, @PathVariable Long id) {
        Test test = testService.findTestById(id);
        model.addAttribute("test", test);
        model.addAttribute("test_id", id);
        return "corrector/showAllQuestions";
    }

    @GetMapping("/tests/{id}/FreeTextQuestion/{id_question}/evaluate")
    public String evaluateFreeTextQuestion(Model model, @PathVariable Long id, @PathVariable Long id_question) {
        boolean hasEnded = testService.hasEnded(id, LocalDateTime.now());
        System.out.println(hasEnded);
        if(!hasEnded){
            throw new TestHasNotEndedYetException("Bearbeitungszeit ist noch nicht abgelaufen");
        }

        Test test = testService.findTestById(id);
        FreeTextQuestion question = freeTextQuestionService.findFreeTextQuestionById(id_question);
        FreeTextAnswer answer = question.getFreeTextAnswer();

        model.addAttribute("test", test);
        model.addAttribute("test_id", id);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "corrector/evaluateFreeTextQuestion";
    }

    @PostMapping("/tests/{id}/FreeTextQuestion/{id_question}/evaluate")
    public String submitEvaluation(
            @PathVariable Long id,
            @PathVariable Long id_question,
            @RequestParam String feedback,
            @RequestParam Integer score,
            RedirectAttributes redirectAttributes) {

        freeTextQuestionService.evaluateFreeTextAnswer(id_question, feedback, score);
        FreeTextQuestion currentQuestion = freeTextQuestionService.findFreeTextQuestionById(id_question);
        redirectAttributes.addFlashAttribute("savedQuestion", currentQuestion);
        return "redirect:/corrector/tests/{id}";
    }

    @ExceptionHandler({TestHasNotEndedYetException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/corrector/tests/{id}";
    }


}
