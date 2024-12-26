package de.propra.exambyte.model;

import jakarta.persistence.*;

import java.util.Map;

@Entity
public class MultipleChoiceQuestion implements Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;

    @ElementCollection
    private Map<String, Boolean> answers;

    private int maxScore;

    private String explanation;

    @ManyToOne
    private Test test;

    public MultipleChoiceQuestion() {

    }

    public MultipleChoiceQuestion(String questionText, Map<String, Boolean> answers, int maxScore, String explanation) {
        this.questionText = questionText;
        this.answers = answers;
        this.maxScore = maxScore;
        this.explanation = explanation;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public String getExplanation() {
        return explanation;
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public String getType() {
        return "MultipleChoiceQuestion";
    }

    public void updateQuestion(String questionText, Map<String, Boolean> answers, int maxScore, String explanation) {
//        if (questionText == null || questionText.isEmpty()) {
//            throw new EmptyInputException("Question text cannot be null or empty");
//        }
//        if (answers == null || answers.isEmpty()) {
//            throw new EmptyInputException("Answers cannot be null or empty");
//        }
//        if (maxScore <= 0) {
//            throw new EmptyInputException("Max score must be greater than zero");
//        }

        this.questionText = questionText;
        this.answers = answers;
        this.maxScore = maxScore;
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "MultipleChoiceQuestionDto{" +
                "questionText='" + questionText + '\'' +
                ", answersOptions=" + answers +
                ", maxScore=" + maxScore +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
