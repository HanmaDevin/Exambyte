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
    public String addMCTest(MCTestForm test, Model model) {
        model.addAttribute("test", test);
        return "tests/addMCTest";
    }

    @PostMapping("/addMCTest")
    public String addMCTest(@Valid MCTestForm test, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasFieldErrors()) {
            return "tests/addMCTest";
        }

        MCTestForm newTest = new MCTestForm(test.aufgabenstellung(),
                test.antworten(), test.punktzahl(),
                test.loesung(), test.erklaerung(),
                test.bearbeitungsBeginn(), test.abgabeZeitpunkt(),
                test.veroeffentlichungsZeitpunkt());

        tests.add(newTest);
        System.out.println(tests);

        redirectAttributes.addFlashAttribute("test", test);

        return "redirect:/tests/addMCTest";
    }

    @GetMapping("/testListe")
    public String testListe(Model model) {
        model.addAttribute("tests", tests);
        return "tests/testListe";
    }
}
