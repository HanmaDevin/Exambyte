package de.propra.exambyte;

public class Aufgabe {
    private final int ID;
    private final String name;
    private final String aufgabenBeschreibung;

    public Aufgabe(int ID, String name, String aufgabenBeschreibung) {
        this.ID = ID;
        this.name = name;
        this.aufgabenBeschreibung = aufgabenBeschreibung;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getAufgabenBeschreibung() {
        return aufgabenBeschreibung;
    }
}
