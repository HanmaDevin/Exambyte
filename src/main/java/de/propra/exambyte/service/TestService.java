package de.propra.exambyte.service;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.model.Question;
import de.propra.exambyte.model.Test;
import de.propra.exambyte.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Test createTest(TestDto testDto) {
        validateTestTimes(testDto);

        Test test = new Test(
                testDto.getTitle(),
                testDto.getStartTime(),
                testDto.getEndTime(),
                testDto.getResultTime());

        return testRepository.save(test);
    }

    public Test updateTest(Long id, TestDto dto) {
        validateTestTimes(dto);
        Test test = findTestById(id);
        test.updateTest(dto.getTitle(), dto.getStartTime(), dto.getEndTime(), dto.getResultTime());
        return testRepository.save(test);
    }

    public void addMultipleChoiceQuestionToTest(Long testId, MultipleChoiceQuestion multipleChoiceQuestion) {
        Test test = findTestById(testId);
        test.addMultipleChoiceQuestion(multipleChoiceQuestion);
        testRepository.save(test);
    }

    public void addFreeTextQuestionToTest(Long testId, FreeTextQuestion freeTextQuestion) {
        Test test = findTestById(testId);
        test.addFreeTextQuestion(freeTextQuestion);
        testRepository.save(test);
    }

    public List<Question> getAllQuestions(Long testId) {
        Test test = findTestById(testId);
        return test.getAllQuestions();
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestionById(Long testId, Long questionId) {
        Test test = findTestById(testId);
        return (MultipleChoiceQuestion) test.getMultipleChoiceQuestions(questionId);
    }

    public FreeTextQuestion getFreeTextQuestionById(Long testId, Long questionId) {
        Test test = findTestById(testId);
        return (FreeTextQuestion) test.getFreeTextQuestions(questionId);
    }

    public Question getQuestionById(Long testId, Long questionId) {
        Test test = findTestById(testId);
        return test.getAllQuestions().stream()
                .filter(question -> question.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new TestNotFoundException("Question with ID " + questionId + " not found"));
    }


    private void validateTestTimes(TestDto testDto) {
        if (testDto.getStartTime() == null || testDto.getEndTime() == null || testDto.getResultTime() == null) {
            throw new EmptyInputException("Alle Felder müssen ausgefüllt sein.");
        }

        if (testDto.getTitle() == null || testDto.getTitle().isEmpty()) {
            throw new EmptyInputException("Titel darf nicht leer sein.");
        }

        if (testDto.getEndTime().isBefore(testDto.getStartTime())) {
            throw new WrongDateInputException("Endzeitpunkt muss nach Startzeitpunkt sein.");
        }

        if (testDto.getResultTime().isBefore(testDto.getEndTime())) {
            throw new WrongDateInputException("Ergebniszeitpunkt muss nach Endzeitpunkt sein.");
        }

        if (testDto.getStartTime().isBefore(java.time.LocalDateTime.now())) {
            throw new WrongDateInputException("Startzeitpunkt muss in der Zukunft liegen.");
        }
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Test findTestById(Long id) {
        return testRepository.findById(id).orElseThrow(() -> new TestNotFoundException("Test not found"));
    }
}
