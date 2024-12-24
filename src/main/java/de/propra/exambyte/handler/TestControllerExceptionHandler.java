package de.propra.exambyte.handler;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TestControllerExceptionHandler {


    @ExceptionHandler(WrongDateInputException.class)
    public String handleWrongDateInput(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("testDto", new TestDto());
        return "test-form";
    }

    @ExceptionHandler(LowerOrEqualZeroException.class)
    public String handleLowerThanZeroException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("freeTextQuestionDto", new FreeTextQuestionDto());
        return "free-text-question-form";
    }

    @ExceptionHandler(EmptyInputException.class)
    public String handleEmptyInputException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("freeTextQuestionDto", new FreeTextQuestionDto());
        return "free-text-question-form";
    }

    @ExceptionHandler(TestNotFoundException.class)
    public String handleTestNotFoundException(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "error/test-not-found";
    }


}

