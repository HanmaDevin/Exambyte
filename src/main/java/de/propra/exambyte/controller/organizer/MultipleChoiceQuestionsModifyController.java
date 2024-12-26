package de.propra.exambyte.controller.organizer;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.service.MultipleChoiceQuestionService;
import de.propra.exambyte.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/organizer/tests")
public class MultipleChoiceQuestionsModifyController {
    private final MultipleChoiceQuestionService multipleChoiceQuestionService;
    private final TestService testService;

    public MultipleChoiceQuestionsModifyController(MultipleChoiceQuestionService multipleChoiceQuestionService, TestService testService) {
        this.multipleChoiceQuestionService = multipleChoiceQuestionService;
        this.testService = testService;
    }

    @GetMapping("/{id_test}/questions/MultipleChoiceQuestion/{id_question}")
    public String modifyMultipleChoiceQuestion(Model model, @PathVariable Long id_test, @PathVariable Long id_question) {
        MultipleChoiceQuestion currentQuestion = multipleChoiceQuestionService.findMultipleChoiceQuestionById(id_question);
        MultipleChoiceQuestionDto multipleChoiceQuestionDto = new MultipleChoiceQuestionDto(currentQuestion.getQuestionText(), currentQuestion.getMaxScore(), currentQuestion.getExplanation(), currentQuestion.getAnswers());
        System.out.println(multipleChoiceQuestionDto);
        model.addAttribute("question", multipleChoiceQuestionDto);
        model.addAttribute("id_test", id_test);
        model.addAttribute("id_question", id_question);

        return "organizer/editQuestions/multiple-choice-questions-modify";
    }

    @PostMapping("/{id_test}/questions/MultipleChoiceQuestion/{id_question}")
    public String saveModifiedMultipleChoiceQuestion(@PathVariable Long id_test,
                                                     @PathVariable Long id_question,
                                                     @ModelAttribute MultipleChoiceQuestionDto modifiedQuestion,
                                                     RedirectAttributes flashAttributes) {

        System.out.println(modifiedQuestion);
        modifiedQuestion.parseAnswers();
        MultipleChoiceQuestion updatedQuestion = multipleChoiceQuestionService.updateMultipleChoiceQuestion(id_question, modifiedQuestion);
        System.out.println(updatedQuestion.toString());
        flashAttributes.addFlashAttribute("updatedQuestion", updatedQuestion);

        return String.format("redirect:/organizer/tests/%d/questions", id_test);
    }

}