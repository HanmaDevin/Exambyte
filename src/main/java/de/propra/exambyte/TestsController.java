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

    private final ArrayList<MCTestForm> mcTests = new ArrayList<>();

    @GetMapping("/addMCTest")
    public String index(MCTestForm mcTestForm, Model model) {
        model.addAttribute("mcTest", mcTestForm);
        return "tests/addMCTest";
    }

    @PostMapping("/addMCTest")
    public String addMCTest(@Valid MCTestForm mcTestForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "tests/addMCTest";
        }

        MCTestForm newTest = new MCTestForm(mcTestForm.aufgabenstellung(),
                mcTestForm.antworten(), mcTestForm.punktzahl(),
                mcTestForm.loesung(), mcTestForm.erklaerung(),
                mcTestForm.bearbeitungsBeginn(), mcTestForm.abgabeZeitpunkt(),
                mcTestForm.veroeffentlichungsZeitpunkt());

        mcTests.add(newTest);
        System.out.println(mcTests);

        redirectAttributes.addFlashAttribute("mcTest", mcTestForm);

        return "redirect:/tests/addMCTest";
    }

    @GetMapping("/testListe")
    public String showMCTestListe(Model model) {
        model.addAttribute("mcTests", mcTests);
        return "tests/testListe";
    }
}
