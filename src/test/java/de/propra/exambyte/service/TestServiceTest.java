package de.propra.exambyte.service;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.model.Question;
import de.propra.exambyte.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestServiceTest {

    private TestRepository testRepository;
    private TestService testService;

    @BeforeEach
    void setUp() {
        testRepository = mock(TestRepository.class);
        testService = new TestService(testRepository);
    }

    @Test
    @DisplayName("Test wird erfolgreich erstellt und gespeichert")
    void test1() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        LocalDateTime resultTime = LocalDateTime.now().plusHours(3);

        TestDto dto = new TestDto("Test Title", startTime, endTime, resultTime);
        de.propra.exambyte.model.Test mockTest = new
                de.propra.exambyte.model.Test(dto.getTitle(), dto.getStartTime(), dto.getEndTime(), dto.getResultTime());

        when(testRepository.save(Mockito.any(de.propra.exambyte.model.Test.class))).thenReturn(mockTest);

        de.propra.exambyte.model.Test result = testService.createTest(dto);

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle());
        assertEquals(startTime, result.getStartTime());
        assertEquals(endTime, result.getEndTime());
        assertEquals(resultTime, result.getResultTime());
        verify(testRepository, times(1)).save(any(de.propra.exambyte.model.Test.class));
    }

    @Test
    @DisplayName("Test mit leeren Feldern löst EmptyInputException aus")
    void test2() {
        TestDto dto = new TestDto(null, null, null, null);

        assertThatThrownBy(() -> testService.createTest(dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Alle Felder müssen ausgefüllt sein.");
    }

    @Test
    @DisplayName("Test mit leerem Titel löst EmptyInputException aus")
    void test2_b() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        LocalDateTime resultTime = LocalDateTime.now().plusHours(3);
        TestDto dto = new TestDto("", startTime, endTime, resultTime);

        assertThatThrownBy(() -> testService.createTest(dto))
                .isInstanceOf(EmptyInputException.class)
                .hasMessage("Titel darf nicht leer sein.");
    }

    @Test
    @DisplayName("Test mit falschen Datum (endTime before startTime) löst WrongDateInputException aus")
    void test3() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(2);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);
        LocalDateTime resultTime = LocalDateTime.now().plusHours(3);
        TestDto dto = new TestDto("Sample Test", startTime, endTime, resultTime);

        assertThatThrownBy(() -> testService.createTest(dto))
                .isInstanceOf(WrongDateInputException.class)
                .hasMessage("Endzeitpunkt muss nach Startzeitpunkt sein.");
    }

    @Test
    @DisplayName("Test mit falschen Datum (ResultTime before endTime) löst WrongDateInputException aus")
    void test3_b() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(3);
        LocalDateTime resultTime = LocalDateTime.now().plusHours(2);
        TestDto dto = new TestDto("Sample Test", startTime, endTime, resultTime);

        assertThatThrownBy(() -> testService.createTest(dto))
                .isInstanceOf(WrongDateInputException.class)
                .hasMessage("Ergebniszeitpunkt muss nach Endzeitpunkt sein.");
    }

    @Test
    @DisplayName("Test mit falschen Datum (starTime must be in the Future) löst WrongDateInputException aus")
    void test3_c() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(2);
        LocalDateTime resultTime = LocalDateTime.now().plusHours(3);

        TestDto dto = new TestDto("Sample Test", startTime, endTime, resultTime);

        assertThatThrownBy(() -> testService.createTest(dto))
                .isInstanceOf(WrongDateInputException.class)
                .hasMessage("Startzeitpunkt muss in der Zukunft liegen.");
    }

    @Test
    @DisplayName("Test wird erfolgreich aktualisiert")
    void test4() {
        Long testId = 1L;
        TestDto dto = new TestDto("Updated Title", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));
        de.propra.exambyte.model.Test existingTest = new
                de.propra.exambyte.model.Test("Old Title", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));

        when(testRepository.findById(testId)).thenReturn(Optional.of(existingTest));
        when(testRepository.save(any(
                de.propra.exambyte.model.Test.class))).thenReturn(existingTest);


        de.propra.exambyte.model.Test result = testService.updateTest(testId, dto);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        verify(testRepository, times(1)).save(any(
                de.propra.exambyte.model.Test.class));
    }

    @Test
    @DisplayName("Test mit nicht valider Id löst TestNotFoundException aus (updateTest)")
    void test5() {
        Long mockTestId = 1L;
        TestDto dto = new TestDto("Updated Title", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(3));

        when(testRepository.findById(mockTestId)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> testService.updateTest(mockTestId, dto))
                .isInstanceOf(TestNotFoundException.class)
                .hasMessage("Test not found");
    }

    @Test
    @DisplayName("Test mit nicht valider Id löst TestNotFoundException aus (findTestById)")
    void test6() {
        Long mockTestId = 1L;
        when(testRepository.findById(mockTestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.findTestById(mockTestId))
                .isInstanceOf(TestNotFoundException.class)
                .hasMessage("Test not found");
    }

    @Test
    @DisplayName("getAllTests funktioniert wie erwartet")
    void test7() {
        when(testRepository.findAll()).thenReturn(List.of(new de.propra.exambyte.model.Test()));

        List<de.propra.exambyte.model.Test> tests = testService.getAllTests();

        assertNotNull(tests);
        assertFalse(tests.isEmpty());
    }

    @Test
    @DisplayName("addMultipleChoiceQuestionToTest funktioniert wie erwartet")
    void test8() {
        Long mockTestId = 1L;
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();
        de.propra.exambyte.model.Test test = new de.propra.exambyte.model.Test();

        when(testRepository.findById(mockTestId)).thenReturn(Optional.of(test));

        testService.addMultipleChoiceQuestionToTest(mockTestId, question);

        verify(testRepository, times(1)).save(test);
        assertTrue(test.getMultipleChoiceQuestions(mockTestId).contains(question));
    }

    @Test
    @DisplayName("addMultipleChoiceQuestionToTest wirft TestNotFoundException wenn TestId invalide")
    void test9() {
        Long mockTestId = 1L;
        MultipleChoiceQuestion question = new MultipleChoiceQuestion();

        when(testRepository.findById(mockTestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.addMultipleChoiceQuestionToTest(mockTestId, question))
                .isInstanceOf(TestNotFoundException.class)
                .hasMessage("Test not found");
    }

    @Test
    @DisplayName("addFreeTextQuestionToTest funktioniert wie erwartet")
    void test10() {
        Long mockTestId = 1L;
        FreeTextQuestion question = new FreeTextQuestion();
        de.propra.exambyte.model.Test test = new de.propra.exambyte.model.Test();


        when(testRepository.findById(mockTestId)).thenReturn(Optional.of(test));

        testService.addFreeTextQuestionToTest(mockTestId, question);

        verify(testRepository, times(1)).save(test);
        assertTrue(test.getFreeTextQuestions(mockTestId).contains(question));
    }


    @Test
    @DisplayName("addFreeTextQuestionToTest wirft TestNotFoundException wenn TestId invalide")
    void test11() {
        Long mockTestId = 1L;
        FreeTextQuestion question = new FreeTextQuestion();

        when(testRepository.findById(mockTestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.addFreeTextQuestionToTest(mockTestId, question))
                .isInstanceOf(TestNotFoundException.class)
                .hasMessage("Test not found");
    }

    @Test
    @DisplayName("getAllQuestions funktioniert wie erwartet")
    void test12() {
        Long mockTestId = 1L;
        de.propra.exambyte.model.Test test = new de.propra.exambyte.model.Test();
        FreeTextQuestion question1 = mock(FreeTextQuestion.class);
        MultipleChoiceQuestion question2 = mock(MultipleChoiceQuestion.class);

        test.addFreeTextQuestion(question1);
        test.addMultipleChoiceQuestion(question2);

        when(testRepository.findById(mockTestId)).thenReturn(Optional.of(test));

        List<Question> questions = testService.getAllQuestions(mockTestId);

        assertEquals(2, questions.size());
        assertTrue(questions.contains(question1));
        assertTrue(questions.contains(question2));
    }

    @Test
    @DisplayName("getAllQuestions wirft TestNotFoundException wenn TestId invalide")
    void test13() {
        Long mockTestId = 1L;

        when(testRepository.findById(mockTestId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testService.getAllQuestions(mockTestId))
                .isInstanceOf(TestNotFoundException.class)
                .hasMessage("Test not found");
    }



}
