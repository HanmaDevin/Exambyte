package de.propra.exambyte.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FreeTextQuestionDtoTest {

    @Test
    @DisplayName("Der Konstruktor und die Getter der Klasse FreeTextQuestionDto funktionieren wie erwartet.")
    public void testFreeTextQuestionDtoConstructorAndGetters() {
        String question = "What is the capital of Germany?";
        int maxScore = 10;
        String possibleAnswer = "Berlin";

        FreeTextQuestionDto dto = new FreeTextQuestionDto(question, maxScore, possibleAnswer);

        assertEquals(question, dto.getQuestionText());
        assertEquals(maxScore, dto.getMaxScore());
        assertEquals(possibleAnswer, dto.getPossibleAnswer());
    }

    @Test
    @DisplayName("Die Setter der Klasse FreeTextQuestionDto funktionieren wie erwartet.")
    public void testFreeTextQuestionDtoSetters() {
        FreeTextQuestionDto dto = new FreeTextQuestionDto();
        String question = "What is the capital of Germany?";
        int maxScore = 10;
        String possibleAnswer = "Berlin";

        dto.setQuestionText(question);
        dto.setMaxScore(maxScore);
        dto.setPossibleAnswer(possibleAnswer);

        assertEquals(question, dto.getQuestionText());
        assertEquals(maxScore, dto.getMaxScore());
        assertEquals(possibleAnswer, dto.getPossibleAnswer());
    }

    @Test
    @DisplayName("Die Methode toString() der Klasse FreeTextQuestionDto funktioniert wie erwartet.")
    public void testFreeTextQuestionDtoToString() {
        String question = "What is the capital of Germany?";
        int maxScore = 10;
        String possibleAnswer = "Berlin";

        FreeTextQuestionDto dto = new FreeTextQuestionDto(question, maxScore, possibleAnswer);
        String expectedString = "FreeTextQuestionDto{questionText='What is the capital of Germany?', maxScore=10, possibleAnswer='Berlin'}";

        assertEquals(expectedString, dto.toString());
    }
}