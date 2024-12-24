package de.propra.exambyte.dto;

import java.util.List;
import java.util.Map;

public class MultipleChoiceQuestionDto {

    private String questionText;
    private Map<String, Boolean> answers;
    private int maxScore;
    private String explanation;

    public MultipleChoiceQuestionDto() {
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }

    public MultipleChoiceQuestionDto(String questionText, int maxScore, String explanation, Map<String, Boolean> answers) {
        this.questionText = questionText;
        this.maxScore = maxScore;
        this.explanation = explanation;
        this.answers = answers;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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

    @Override
    public String toString() {
        return "MultipleChoiceQuestionDto{" +
                "questionText='" + questionText + '\'' +
        ", answersOptions=" + answers +
                ", maxScore=" + maxScore +
                ", explanation='" + explanation + '\''+
        '}';
    }
}
