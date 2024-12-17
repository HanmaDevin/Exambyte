package de.propra.exambyte.test_typen;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public class MCTestForm {
    @NotBlank(message = "Aufgabenstellung darf nicht leer sein")
    private final String aufgabenstellung;

    @NotNull(message = "Antworten dürfen nicht leer sein")
    //@Pattern(regexp = "")
    private final List<String> antworten;

    @NotNull(message = "Punktzahl darf nicht leer sein")
    @Min(value = 1, message = "Punktzahl muss mindestens 1 sein")
    private final Integer punktzahl;

    @NotNull(message = "Lösung darf nicht leer sein")
    private final List<String> loesung;

    @NotBlank(message = "Erklärung darf nicht leer sein")
    private final String erklaerung;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @FutureOrPresent(message = "Bearbeitungsbeginn muss in der Zukunft oder der Gegenwart liegen")
    private final LocalDateTime bearbeitungsBeginn;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Future(message = "Abgabezeitpunkt muss in der Zukunft liegen")
    private final LocalDateTime abgabeZeitpunkt;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")
    @Future(message = "Veröffentlichungszeitpunkt muss in der Zukunft liegen")
    private final LocalDateTime veroeffentlichungsZeitpunkt;

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

    public String getAufgabenstellung() {
        return aufgabenstellung;
    }

    public List<String> getAntworten() {
        return antworten;
    }

    public Integer getPunktzahl() {
        return punktzahl;
    }

    public List<String> getLoesung() {
        return loesung;
    }

    public String getErklaerung() {
        return erklaerung;
    }

    public LocalDateTime getBearbeitungsBeginn() {
        return bearbeitungsBeginn;
    }

    public LocalDateTime getAbgabeZeitpunkt() {
        return abgabeZeitpunkt;
    }

    public LocalDateTime getVeroeffentlichungsZeitpunkt() {
        return veroeffentlichungsZeitpunkt;
    }

    @Override
    public String toString() {
        return "MCTestForm{" +
                "aufgabenstellung='" + aufgabenstellung + '\'' +
                ", antworten=" + antworten +
                ", punktzahl=" + punktzahl +
                ", loesung=" + loesung +
                ", erklaerung='" + erklaerung + '\'' +
                ", bearbeitungsBeginn=" + bearbeitungsBeginn +
                ", abgabeZeitpunkt=" + abgabeZeitpunkt +
                ", veroeffentlichungsZeitpunkt=" + veroeffentlichungsZeitpunkt +
                '}';
    }
}