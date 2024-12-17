package de.propra.exambyte;

import de.propra.exambyte.test_typen.MCTestForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;

// This class is only for logik testing, could change a lot

@Controller
@RequestMapping("/tests")
public class TestsController {

    private final ArrayList<MCTestForm> tests = new ArrayList<>();

    @GetMapping("/addMCTest")
    public String addMCTest(MCTestForm testForm, Model model) {
        model.addAttribute("testForm", testForm);
        return "tests/addMCTest";
    }

    @PostMapping("/addMCTest")
    public String addMCTest(@Valid MCTestForm testForm, BindingResult result, RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "tests/addMCTest";
        }

        MCTestForm newTest = new MCTestForm(testForm.getAufgabenstellung(),
                testForm.getAntworten(), testForm.getPunktzahl(),
                testForm.getLoesung(), testForm.getErklaerung(),
                testForm.getBearbeitungsBeginn(), testForm.getAbgabeZeitpunkt(),
                testForm.getVeroeffentlichungsZeitpunkt());

        tests.add(newTest);
        System.out.println(tests);

        redirectAttributes.addFlashAttribute("testForm", testForm);

        return "redirect:/tests/addMCTest";
    }

    @GetMapping("/testListe")
    public String testsListe(Model model) {
        model.addAttribute("tests", tests);
        return "tests/testListe";
    }
}
