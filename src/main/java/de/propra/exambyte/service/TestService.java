package de.propra.exambyte.service;

import de.propra.exambyte.dto.TestDto;
import de.propra.exambyte.exception.EmptyInputException;
import de.propra.exambyte.exception.TestNotFoundException;
import de.propra.exambyte.exception.WrongDateInputException;
import de.propra.exambyte.model.*;
import de.propra.exambyte.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Question getQuestionById(Long testId, Long questionId) {
        Test test = findTestById(testId);
        return test.getAllQuestions().stream()
                .filter(question -> question.getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new TestNotFoundException("Question with ID " + questionId + " not found"));
    }

    public double calculateTotalEarnedPoints(Long testId) {
        Test test = findTestById(testId);
        double totalEarned = 0;
        for (Question q : test.getAllQuestions()) {
            if (q instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                MultipleChoiceAnswer answer = mcq.getMultipleChoiceAnswer();
                if (answer != null) {
                    Set<String> selected = answer.getSelectedAnswers();
                    Set<String> correct = mcq.getCorrectAnswers();
                    Set<String> symDiff = new HashSet<>(correct);
                    symDiff.addAll(selected);
                    Set<String> intersection = new HashSet<>(correct);
                    intersection.retainAll(selected);
                    symDiff.removeAll(intersection);
                    int errorCount = symDiff.size();

                    double maxPoints = mcq.getMaxScore();
                    double earnedPoints;
                    if (errorCount == 0) {
                        earnedPoints = maxPoints;
                    } else if (errorCount == 1) {
                        earnedPoints = maxPoints / 2.0;
                    } else {
                        earnedPoints = 0;
                    }
                    totalEarned += earnedPoints;
                }
            } else if (q instanceof FreeTextQuestion) {
                FreeTextQuestion ftq = (FreeTextQuestion) q;
                if (ftq.getFreeTextAnswer() != null) {
                    totalEarned += ftq.getFreeTextAnswer().getScore();
                }
            }
        }
        return totalEarned;
    }

    public double calculateTotalMaxPoints(Test test) {
        return test.getAllQuestions().stream()
                .mapToDouble(Question::getMaxScore)
                .sum();
    }




    //prüfen, ob Test schon aktiv ist
    public boolean isActive(Long id, LocalDateTime now) {
        Test test = findTestById(id);
        System.out.println(now);
        return now.isAfter(test.getStartTime()) && now.isBefore(test.getEndTime());
    }

    //prüfen, ob Test beendet ist
    public boolean hasEnded(Long id, LocalDateTime now) {
        Test test = findTestById(id);
        return now.isAfter(test.getEndTime());
    }

    public boolean resultDue(Long id, LocalDateTime now) {
        Test test = findTestById(id);
        return now.isAfter(test.getResultTime());
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

    public void setTestWorkedOnToTrue(Test test) {
        test.setTestWorkedOn(true);
        testRepository.save(test);
    }

    public void setGithubHandle(Test test, String githubHandle) {
        test.setWorkedOnBy(githubHandle);
        testRepository.save(test);
    }
}
