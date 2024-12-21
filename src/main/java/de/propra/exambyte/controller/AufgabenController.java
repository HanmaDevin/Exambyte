package de.propra.exambyte.controller;

import de.propra.exambyte.model.test_typen.Aufgabe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;



@Controller
@RequestMapping("/aufgaben")
public class AufgabenController {

    private final ArrayList<Aufgabe> aufgaben = new ArrayList<>();

    @GetMapping("/aufgabenListe")
    public String bearbeiten(Model model) {
        aufgaben.add(new Aufgabe(0, "Aufgabe 1", "aufgabenBeschreibung 1"));
        aufgaben.add(new Aufgabe(1, "Aufgabe 2", "aufgabenBeschreibung 2"));
        aufgaben.add(new Aufgabe(2, "Aufgabe 3", "aufgabenBeschreibung 3"));
        aufgaben.add(new Aufgabe(3, "Aufgabe 4", "aufgabenBeschreibung 4"));
        aufgaben.add(new Aufgabe(4, "Aufgabe 5", "aufgabenBeschreibung 5"));

        model.addAttribute("aufgaben", aufgaben);

        return "aufgaben/aufgabenListe";
    }

    @GetMapping("/aufgabe/{id}")
    public String aufgabe(Model model, @PathVariable int id) {
        model.addAttribute("titel", aufgaben.get(id).getName());
        model.addAttribute("aufgaben", aufgaben);
        model.addAttribute("aufgabenBeschreibung", aufgaben.get(id).getAufgabenBeschreibung());

        return "aufgaben/aufgabe";
    }
}
