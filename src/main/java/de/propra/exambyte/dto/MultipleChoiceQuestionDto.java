package de.propra.exambyte.dto;

import java.util.List;

public class MultipleChoiceQuestionDto {

    private String questionText;
    private List<String> answersOptions;
    private List<Integer> correctAnswers;
    private int maxScore;
    private String explanation;

    public MultipleChoiceQuestionDto() {
    }

    public MultipleChoiceQuestionDto(String questionText, int maxScore, String explanation, List<String> answersOptions, List<Integer> correctAnswers) {
        this.questionText = questionText;
        this.maxScore = maxScore;
        this.explanation = explanation;
        this.answersOptions = answersOptions;
        this.correctAnswers = correctAnswers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getAnswersOptions() {
        return answersOptions;
    }

    public void setAnswersOptions(List<String> answersOptions) {
        this.answersOptions = answersOptions;
    }

    public List<Integer> getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(List<Integer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
