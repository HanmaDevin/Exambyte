package de.propra.exambyte.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FreeTextQuestionTest {

    @Test
    @DisplayName("Konstruktor Test")
    void test1(){
        FreeTextQuestion freeTextQuestion = new FreeTextQuestion("Frage", 10, "Antwort");
        assertEquals("Frage", freeTextQuestion.getQuestionText());
        assertEquals(10, freeTextQuestion.getMaxScore());
        assertEquals("Antwort", freeTextQuestion.getPossibleAnswer());
    }

    @Test
    @DisplayName("updateQuestion Test")
    void test2() {
        FreeTextQuestion question = new FreeTextQuestion();

        question.updateQuestion("Frage", 10,"Antwort");

        assertEquals("Frage", question.getQuestionText());
        assertEquals(10, question.getMaxScore());
        assertEquals("Antwort", question.getPossibleAnswer());
    }

    @Test
    @DisplayName("getType Test")
    void test3() {
        FreeTextQuestion question = new FreeTextQuestion();
        assertEquals("FreeTextQuestion", question.getType());
    }

    @Test
    @DisplayName("toString Test")
    void test4() {
        FreeTextQuestion question = new FreeTextQuestion("Updated Question", 15, "Updated Answer");
        String expected = "FreeTextQuestion{id=null, questionText='Updated Question', maxScore=15, possibleAnswer='Updated Answer'}";
        assertEquals(expected, question.toString());
    }
}
