package de.propra.exambyte.service;

import de.propra.exambyte.dto.FreeTextQuestionDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.repository.FreeTextAnswerRepository;
import de.propra.exambyte.repository.FreeTextQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class FreeTextQuestionServiceTest {

    private FreeTextQuestionService freeTextQuestionService;
    private FreeTextQuestionRepository freeTextQuestionRepository;
    private FreeTextAnswerRepository freeTextAnswerRepository;


    @BeforeEach
    void setUp() {
        freeTextQuestionRepository = mock(FreeTextQuestionRepository.class);
        freeTextQuestionService = new FreeTextQuestionService(freeTextQuestionRepository, freeTextAnswerRepository);
    }

    @Test
    @DisplayName("FreeTextQuestion mit validen Daten wird erfolgreich erstellt und gespeichert")
    void test1() {
        FreeTextQuestionDto dto = new FreeTextQuestionDto("Valid Question", 10, "Valid Answer");
        FreeTextQuestion expectedQuestion = new FreeTextQuestion("Valid Question", 10, "Valid Answer");

        Mockito.when(freeTextQuestionRepository.save(Mockito.any(FreeTextQuestion.class))).thenReturn(expectedQuestion);

        FreeTextQuestion actualQuestion = freeTextQuestionService.createFreeTextQuestion(dto);

        assertThat(actualQuestion.getQuestionText()).isEqualTo(expectedQuestion.getQuestionText());
        assertThat(actualQuestion.getMaxScore()).isEqualTo(expectedQuestion.getMaxScore());
        assertThat(actualQuestion.getPossibleAnswer()).isEqualTo(expectedQuestion.getPossibleAnswer());

        verify(freeTextQuestionRepository, times(1)).save(Mockito.any(FreeTextQuestion.class));

    }

    @Test
    @DisplayName("Update FreeTextQuestion funktioniert wie erwartet")
    void test2() {
        Long id = 1L;
        FreeTextQuestion existingQuestion = new FreeTextQuestion("Old Question", 5, "Old Answer");
        FreeTextQuestionDto dto = new FreeTextQuestionDto("New Question", 10, "New Answer");
        FreeTextQuestion updatedQuestion = new FreeTextQuestion("New Question", 10, "New Answer");

        when(freeTextQuestionRepository.findById(id)).thenReturn(Optional.of(existingQuestion));
        when(freeTextQuestionRepository.save(existingQuestion)).thenReturn(updatedQuestion);

        FreeTextQuestion result = freeTextQuestionService.updateFreeTextQuestion(id, dto);

        assertThat(result.getQuestionText()).isEqualTo(dto.getQuestionText());
        assertThat(result.getPossibleAnswer()).isEqualTo(dto.getPossibleAnswer());
        assertThat(result.getMaxScore()).isEqualTo(dto.getMaxScore());

        verify(freeTextQuestionRepository, times(1)).findById(id);
        verify(freeTextQuestionRepository, times(1)).save(existingQuestion);
    }


    @Test
    @DisplayName("updateFreeTextQuestion wirft richtige Exception: leerer Fragestellung")
    void test3() {
        long mockId = 1L;
        FreeTextQuestionDto dto = new FreeTextQuestionDto("", 10, "Valid Answer");

        assertThatThrownBy(() -> freeTextQuestionService.updateFreeTextQuestion(mockId, dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Die Fragestellung darf nicht leer sein");

    }

    @Test
    @DisplayName("updateFreeTextQuestion wirft richtige Exception: leerer Antwort")
    void test3_b() {
        long mockId = 1L;
        FreeTextQuestionDto dto = new FreeTextQuestionDto("Valid Question", 10, "");

        assertThatThrownBy(() -> freeTextQuestionService.updateFreeTextQuestion(mockId, dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Antworten dürfen nicht leer sein");

    }

    @Test
    @DisplayName("updateFreeTextQuestion wirft richtige Exception: maxScore ist kleiner oder gleich 0")
    void test3_c() {
        long mockId = 1L;
        FreeTextQuestionDto dto = new FreeTextQuestionDto("Valid Question", 0, "Valid Answer");

        assertThatThrownBy(() -> freeTextQuestionService.updateFreeTextQuestion(mockId, dto))
                .isInstanceOf(LowerOrEqualZeroException.class)
                .hasMessage("Punktzahl muss größer als 0 sein");

    }
}
