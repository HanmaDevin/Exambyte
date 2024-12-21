package de.propra.exambyte.model.test_typen;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record MCTestForm(@NotBlank(message = "Aufgabenstellung darf nicht leer sein") String aufgabenstellung,
                         @NotNull(message = "Antworten dürfen nicht leer sein") List<String> antworten,
                         @NotNull(message = "Punktzahl darf nicht leer sein") @Min(value = 1, message = "Punktzahl muss mindestens 1 sein") Integer punktzahl,
                         @NotNull(message = "Lösung darf nicht leer sein") List<String> loesung,
                         @NotBlank(message = "Erklärung darf nicht leer sein") String erklaerung,
                         @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") @Future(message = "Bearbeitungsbeginn muss in der Zukunft oder der Gegenwart liegen") LocalDateTime bearbeitungsBeginn,
                         @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") @Future(message = "Abgabezeitpunkt muss in der Zukunft liegen") LocalDateTime abgabeZeitpunkt,
                         @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") @Future(message = "Veröffentlichungszeitpunkt muss in der Zukunft liegen") LocalDateTime veroeffentlichungsZeitpunkt) {


    public MCTestForm(String aufgabenstellung, List<String> antworten, Integer punktzahl, List<String> loesung, String erklaerung, LocalDateTime bearbeitungsBeginn, LocalDateTime abgabeZeitpunkt, LocalDateTime veroeffentlichungsZeitpunkt) {
        this.aufgabenstellung = aufgabenstellung;
        this.antworten = antworten;
        this.punktzahl = punktzahl;
        this.loesung = loesung;
        this.erklaerung = erklaerung;
        this.bearbeitungsBeginn = bearbeitungsBeginn;
        this.abgabeZeitpunkt = abgabeZeitpunkt;
        this.veroeffentlichungsZeitpunkt = veroeffentlichungsZeitpunkt;
    }

    @Override
    public String toString() {
        return "Test {" + aufgabenstellung +
                ", Antworten = " + antworten +
                ", Punktzahl = " + punktzahl +
                ", Loesung = " + loesung +
                ", Erklaerung = " + erklaerung +
                ", BearbeitungsBeginn = " + bearbeitungsBeginn +
                ", abgabeZeitpunkt = " + abgabeZeitpunkt +
                ", Veroeffentlichungs Zeitpunkt = " + veroeffentlichungsZeitpunkt +
                '}';
    }
}