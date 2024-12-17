package de.propra.exambyte.test_typen;

import java.time.LocalDateTime;
import java.util.List;

public record FreiTextTestForm(
        String aufgabenstellung, List<String> fragen,
        int punktzahl, String loesungsVorschlag, LocalDateTime bearbeitungsBeginn, LocalDateTime abgabeZeitpunkt,
        LocalDateTime veroeffentlichungsZeitpunkt) {
}
