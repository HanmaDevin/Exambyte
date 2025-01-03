package de.propra.exambyte.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MultipleChoiceQuestionTest {

    @Test
    @DisplayName("Konstruktor Test")
    void test1(){
        MultipleChoiceQuestion multipleChoiceQuestion = new MultipleChoiceQuestion("Frage", 10, "Erklärung", Map.of("1", true));
        assertEquals("Frage", multipleChoiceQuestion.getQuestionText());
        assertEquals(10, multipleChoiceQuestion.getMaxScore());
        assertEquals("Erklärung", multipleChoiceQuestion.getExplanation());
        assertEquals(Map.of("1", true), multipleChoiceQuestion.getAnswers());
    }



}
