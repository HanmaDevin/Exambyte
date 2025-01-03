package de.propra.exambyte.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class FreeTextAnswerDtoTest {


    @Test
    @DisplayName("The constructor and the getters of the class FreeTextAnswerDto work as expected for the no-argument constructor.")
    void test_noArgsConstructorCreatesDefaultValues() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto();
        assertNull(dto.getAnswer());
        assertEquals(0, dto.getScore());
    }

    @Test
    @DisplayName("The constructor and the getters of the class FreeTextAnswerDto work as expected.")
    void test_allArgsConstructorSetsValuesCorrectly() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto("Sample Answer", 5);
        assertEquals("Sample Answer", dto.getAnswer());
        assertEquals(5, dto.getScore());
    }

    @Test
    @DisplayName("The setters and getters of the class FreeTextAnswerDto work as expected.")
    void test_settersAndGetters() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto();
        dto.setAnswer("Test Answer");
        dto.setScore(10);

        assertEquals("Test Answer", dto.getAnswer());
        assertEquals(10, dto.getScore());
    }

    @Test
    @DisplayName("The toString() method of the class FreeTextAnswerDto works as expected.")
    void test_toStringOutputsCorrectFormat() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto("Example", 8);
        String expected = "FreeTextAnswerDto{answer='Example', score=8}";
        assertEquals(expected, dto.toString());
    }

    @Test
    @DisplayName("The class FreeTextAnswerDto handles null and empty answers correctly.")
    void test_nullAndEmptyAnswerHandling() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto(null, 0);
        assertNull(dto.getAnswer());

        dto.setAnswer("");
        assertEquals("", dto.getAnswer());
    }


}
