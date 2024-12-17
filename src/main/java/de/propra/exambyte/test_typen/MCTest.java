package de.propra.exambyte.test_typen;

import java.time.LocalDateTime;
import java.util.Set;

public record MCTest(String aufgabenstellung, Set<String> antworten, int punktzahl, Set<String> loesung, String erklearung, LocalDateTime bearbeitungsBeginn, LocalDateTime abgabeZeitpunkt) {
    public MCTest {
        if (aufgabenstellung.isBlank()) {
            throw new IllegalArgumentException("Aufgabenstellung darf nicht leer sein");
        }
        if (antworten.isEmpty()) {
            throw new IllegalArgumentException("Antworten dürfen nicht leer sein");
        }
        if (punktzahl < 0) {
            throw new IllegalArgumentException("Punktzahl darf nicht negativ sein");
        }
        if (loesung.isEmpty()) {
            throw new IllegalArgumentException("Lösung darf nicht leer sein");
        }
        if (erklearung.isBlank()) {
            throw new IllegalArgumentException("Erklärung darf nicht leer sein");
        }
    }
}
