package de.propra.exambyte;

import de.propra.exambyte.config.SecurityConfig;
import de.propra.exambyte.dto.FreeTextAnswerDto;
import de.propra.exambyte.exception.FreeTextAnswerNotFoundException;
import de.propra.exambyte.exception.LowerOrEqualZeroException;
import de.propra.exambyte.model.FreeTextAnswer;
import de.propra.exambyte.repository.FreeTextAnswerRepository;
import de.propra.exambyte.service.FreeTextAnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(SecurityConfig.class)
public class FreeTextQuestionServiceTest {


    FreeTextAnswerRepository repository;

    FreeTextAnswerService service;

    @BeforeEach
    void setUp() {
        repository = mock(FreeTextAnswerRepository.class);
        service = new FreeTextAnswerService(repository);
    }


    @Test
    @DisplayName("Erstellt eine FreeTextAnswer erfolgreich und speichert sie")
    void testCreateFreeTextAnswerSuccessfully() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto("Valid Answer", 10);
        FreeTextAnswer savedAnswer = new FreeTextAnswer(dto.getAnswer(), dto.getScore());

        when(repository.save(any(FreeTextAnswer.class))).thenReturn(savedAnswer);
        FreeTextAnswer result = service.createFreeTextAnswer(dto);

        assertNotNull(result);
        assertEquals("Valid Answer", result.getAnswer());
        assertEquals(10, result.getScore());
        verify(repository, times(1)).save(any(FreeTextAnswer.class));
    }


    @Test
    @DisplayName("Findet FreeTextAnswer erfolgreich per ID")
    void testFindFreeTextAnswerByIdSuccess() {
        Long mockID = 1L;

        FreeTextAnswer answer = new FreeTextAnswer("Sample Answer", 10);
        when(repository.findById(mockID)).thenReturn(Optional.of(answer));
        FreeTextAnswer result = service.findFreeTextAnswerById(mockID);


        // Assert
        assertNotNull(result);
        assertEquals("Sample Answer", result.getAnswer());
        assertEquals(10, result.getScore());
    }


@Test
@DisplayName("Wirft FreeTextAnswerNotFoundException bei nicht gefundener FreeTextAnswer")
void testFindFreeTextAnswerByIdThrowsException() {
    Long mockID = 1L;
        when(repository.findById(mockID)).thenReturn(Optional.empty());

    Exception exception = assertThrows(FreeTextAnswerNotFoundException.class, () -> {
        service.findFreeTextAnswerById(mockID);
    });

    assertEquals("Freitextantwort wurde nicht gefunden", exception.getMessage());
}




    @Test
    @DisplayName("Wirft LowerOrEqualZeroException bei negativem Score")
    void testCreateFreeTextAnswerThrowsExceptionForInvalidScore() {
        FreeTextAnswerDto dto = new FreeTextAnswerDto("Invalid Answer", -5);

        Exception exception = assertThrows(LowerOrEqualZeroException.class, () -> {
            service.createFreeTextAnswer(dto);
        });

        assertEquals("Punktzahl darf nicht negativ sein", exception.getMessage());
    }




}
