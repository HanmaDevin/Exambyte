package de.propra.exambyte.service;

import de.propra.exambyte.dto.MultipleChoiceQuestionDto;
import de.propra.exambyte.exception.*;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.repository.MultipleChoiceQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class MultipleChoiceQuestionServiceTest {

    private MultipleChoiceQuestionService multipleChoiceQuestionService;
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;

    @BeforeEach
    void setUp() {
        multipleChoiceQuestionRepository = mock(MultipleChoiceQuestionRepository.class);
        multipleChoiceQuestionService = new MultipleChoiceQuestionService(multipleChoiceQuestionRepository);
    }

    @Test
    @DisplayName("MultipleChoiceQuestion wird erfolgreich erstellt und gespeichert")
    void testCreateValidMultipleChoiceQuestion() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", true);
        answers.put("Answer 2", false);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", 10, "Explanation", answers);
        MultipleChoiceQuestion expectedQuestion = new MultipleChoiceQuestion("Valid Question", 10, "Explanation", answers);

        when(multipleChoiceQuestionRepository.save(any(MultipleChoiceQuestion.class))).thenReturn(expectedQuestion);

        MultipleChoiceQuestion actualQuestion = multipleChoiceQuestionService.createMultipleChoiceQuestion(dto);

        assertThat(actualQuestion.getQuestionText()).isEqualTo(expectedQuestion.getQuestionText());
        assertThat(actualQuestion.getMaxScore()).isEqualTo(expectedQuestion.getMaxScore());
        assertThat(actualQuestion.getExplanation()).isEqualTo(expectedQuestion.getExplanation());
        assertThat(actualQuestion.getAnswers()).isEqualTo(expectedQuestion.getAnswers());

        verify(multipleChoiceQuestionRepository, times(1)).save(any(MultipleChoiceQuestion.class));
    }

    @Test
    @DisplayName("Update MultipleChoiceQuestion entfernt Antworten wie erwartet")
    void testUpdateMultipleChoiceQuestionWithDeletedAnswers() {
        Long id = 1L;

        // Initiale Antworten
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Old Answer 1", false);
        answers.put("Old Answer 2", true);

        MultipleChoiceQuestion existingQuestion = new MultipleChoiceQuestion("Old Question", 5, "Old Explanation", answers);

        // Aktualisierte Antworten
        Map<String, Boolean> updatedAnswers = new HashMap<>();
        updatedAnswers.put("old Answer 1", true);
        updatedAnswers.put("new Answer 2", false);  // Neue Antwort
        updatedAnswers.put("new Answer 3", true);  // Neue Antwort

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("New Question", 10, "New Explanation", updatedAnswers);
        MultipleChoiceQuestion updatedQuestion = new MultipleChoiceQuestion("New Question", 10, "New Explanation", updatedAnswers);

        // Zu löschende Antworten
        List<String> deletedAnswers = List.of("Old Answer 2");

        when(multipleChoiceQuestionRepository.findById(id)).thenReturn(Optional.of(existingQuestion));
        when(multipleChoiceQuestionRepository.save(existingQuestion)).thenReturn(updatedQuestion);

        MultipleChoiceQuestion result = multipleChoiceQuestionService.updateMultipleChoiceQuestion(id, dto, deletedAnswers);

        assertThat(result.getQuestionText()).isEqualTo(dto.getQuestionText());
        assertThat(result.getMaxScore()).isEqualTo(dto.getMaxScore());
        assertThat(result.getExplanation()).isEqualTo(dto.getExplanation());

        // Prüfen, dass die gelöschte Antwort nicht mehr existiert
        assertThat(result.getAnswers()).doesNotContainKey("Old Answer 2");

        // Prüfen, dass die neue Antwort vorhanden ist
        assertThat(result.getAnswers()).containsEntry("old Answer 1", true);
        assertThat(result.getAnswers()).containsEntry("new Answer 2", false);
        assertThat(result.getAnswers()).containsEntry("new Answer 3", true);

        verify(multipleChoiceQuestionRepository, times(1)).findById(id);
        verify(multipleChoiceQuestionRepository, times(1)).save(existingQuestion);
    }



    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: leere Frage")
    void testCreateThrowsExceptionOnEmptyQuestionText() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", true);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("", 10, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Frage darf nicht leer sein");
    }

    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: keine Antworten")
    void testCreateThrowsExceptionOnEmptyAnswers() {
        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", 10, "Explanation", new HashMap<>());

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Antworten dürfen nicht leer sein");
    }


    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: keine korrekten Antworten")
    void testCreateThrowsExceptionOnNoCorrectAnswers() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", false);
        answers.put("Answer 2", false);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", 10, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(NoAnswersMarkedCorrectException.class)
                .hasMessage("Mindestens eine Antwort muss als korrekt markiert werden.");
    }

    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: zu wenige Antworten")
    void testCreateThrowsExceptionOnInsufficientAnswers() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", true);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", 10, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(InsufficientAnswersException.class)
                .hasMessage("Es müssen mindestens 2 Antworten vorhanden sein");
    }

    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: Punktanzahl ist kleiner oder gleich 0")
    void testCreateThrowsExceptionOnLowerOrEqualZeroScore() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", true);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", 0, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(LowerOrEqualZeroException.class)
                .hasMessage("Punktanzahl muss größer als 0 sein");
    }

    @Test
    @DisplayName("createMultipleChoiceQuestion wirft Exception: Punktanzahl ist null")
    void testCreateThrowsExceptionOnNullScore() {
        Map<String, Boolean> answers = new HashMap<>();
        answers.put("Answer 1", true);

        MultipleChoiceQuestionDto dto = new MultipleChoiceQuestionDto("Valid Question", null, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.createMultipleChoiceQuestion(dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Punktanzahl darf nicht leer sein");
    }


    //geht noch nicht, duplikatvalidierung muss vom controller auf service verschoben werden
    @Test
    @DisplayName("validateMultipleChoiceQuestion wirft Exception: Duplikate in Antworten")
    @Disabled
    void testValidateThrowsExceptionOnDuplicateAnswers() {
        Map<String, Boolean> answers = new LinkedHashMap<>();
        answers.put("Answer 1", true);
        answers.put("Answer 2", false);
        answers.put("Answer 1", true);  // Duplikat wird hinzugefügt (überschreibt nicht in der Map direkt)

        MultipleChoiceQuestion question = new MultipleChoiceQuestion("Valid Question", 10, "Explanation", answers);

        assertThatThrownBy(() -> multipleChoiceQuestionService.validateMultipleChoiceQuestion(question))
                .isInstanceOf(DuplicateAnswerException.class)
                .hasMessage("Antworten dürfen nicht mehrfach vorkommen");
    }







}
