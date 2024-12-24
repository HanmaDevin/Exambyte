package de.propra.exambyte.service;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.FreeTextQuestion;
import de.propra.exambyte.model.MultipleChoiceQuestion;
import de.propra.exambyte.model.Questions;
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

    public void addMultipleChoiceQuestionToTest(Long testId, MultipleChoiceQuestion multipleChoiceQuestion) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException("Test not found"));

        test.addMultipleChoiceQuestion(multipleChoiceQuestion);
        testRepository.save(test);
    }

    public void addFreeTextQuestionToTest(Long testId, FreeTextQuestion freeTextQuestion) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException("Test not found"));

        test.addFreeTextQuestion(freeTextQuestion);
        testRepository.save(test);
    }

    public List<Questions> getAllQuestions(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException("Test not found"));

        return test.getAllQuestions();
    }


    private void validateTestTimes(TestDto testDto) {
        if (testDto.getStartTime() == null || testDto.getEndTime() == null || testDto.getResultTime() == null) {
            throw new WrongDateInputException("All fields must be provided.");
        }

        if (testDto.getEndTime().isBefore(testDto.getStartTime())) {
            throw new WrongDateInputException("End time must be after start time.");
        }

        if (testDto.getResultTime().isBefore(testDto.getEndTime())) {
            throw new WrongDateInputException("Result time must be after end time.");
        }

        if (testDto.getStartTime().isBefore(java.time.LocalDateTime.now())) {
            throw new WrongDateInputException("Start time must be in the future.");
        }
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Object findTestById(Long id) {
        return testRepository.findById(id).orElseThrow(() -> new TestNotFoundException("Test not found"));
    }
}
