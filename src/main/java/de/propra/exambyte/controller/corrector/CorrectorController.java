package de.propra.exambyte.controller.corrector;

import de.propra.exambyte.model.Test;
import de.propra.exambyte.service.TestService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@Secured("ROLE_CORRECTOR")
@RequestMapping("/corrector")
public class CorrectorController {

    private final TestService testService;

    public CorrectorController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/tests")
    public String index(Model model) {
        Test test = testService.getAllTests().get(0);
        System.out.println(test.getWorkedOnBy());
        model.addAttribute("tests", testService.getAllTests());
        return "corrector/corrector";
    }
}
