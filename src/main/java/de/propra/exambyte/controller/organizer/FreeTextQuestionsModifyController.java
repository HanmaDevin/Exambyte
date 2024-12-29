package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.service.FreeTextQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organizer/tests")
@Secured("ROLE_ORGANIZER")
public class FreeTextQuestionsModifyController {

    private final FreeTextQuestionService freeTextQuestionService;
    private final TestService testService;

    public FreeTextQuestionsModifyController(FreeTextQuestionService freeTextQuestionService, TestService testService) {
        this.freeTextQuestionService = freeTextQuestionService;
        this.testService = testService;
    }

    @GetMapping("/{id_test}/questions/FreeTextQuestion/{id_question}")
    public String modifyFreeTextQuestion(@PathVariable Long id_test,
                                         @PathVariable Long id_question,
                                         Model model) {
        FreeTextQuestion currentQuestion = freeTextQuestionService.findFreeTextQuestionById(id_question);
        FreeTextQuestionDto freeTextQuestionDto = new FreeTextQuestionDto(currentQuestion.getQuestionText(), currentQuestion.getMaxScore(), currentQuestion.getPossibleAnswer());
        System.out.println(currentQuestion);

        model.addAttribute("question", freeTextQuestionDto);
        model.addAttribute("id_test", id_test);
        model.addAttribute("id_question", id_question);

        return "organizer/editQuestions/free-text-questions-modify";
    }

    @PostMapping("/{id_test}/questions/FreeTextQuestion/{id_question}")
    public String saveModifiedFreeTextQuestion(@PathVariable Long id_test,
                                               @PathVariable Long id_question,
                                               @ModelAttribute FreeTextQuestionDto modifiedQuestion,
                                               RedirectAttributes flashAttributes) {
        FreeTextQuestion updatedQuestion = freeTextQuestionService.updateFreeTextQuestion(id_question, modifiedQuestion);
        System.out.println(updatedQuestion.toString());
        flashAttributes.addFlashAttribute("updatedQuestion", updatedQuestion);

        return String.format("redirect:/organizer/tests/%d/questions", id_test);
    }

    @ExceptionHandler({LowerOrEqualZeroException.class, EmptyInputException.class})
    public String handleExceptions(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/organizer/tests/{id_test}/questions/FreeTextQuestion/{id_question}";
    }
}
