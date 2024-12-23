package de.propra.exambyte.handler;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.WrongDateInput;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class TestControllerExceptionHandler {


    @ExceptionHandler(WrongDateInput.class)
    public String handleWrongDateInput(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("testDto", new TestDto());
        return "test-form";
    }
}
