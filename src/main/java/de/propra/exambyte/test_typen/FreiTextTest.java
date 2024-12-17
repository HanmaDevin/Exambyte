package de.propra.exambyte.test_typen;

import java.time.LocalDateTime;

public record FreiTextTest(String aufgabenstellung, int punktzahl, String loesungsVorschlag, LocalDateTime bearbeitungsBeginn, LocalDateTime abgabeZeitpunkt) {
}
