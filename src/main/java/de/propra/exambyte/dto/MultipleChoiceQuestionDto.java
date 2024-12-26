package de.propra.exambyte.dto;

import de.propra.exambyte.exception.DuplicateAnswerException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MultipleChoiceQuestionDto {

    private String questionText;
    private Map<String, Boolean> answers;
    private int maxScore;
    private String explanation;

    private List<String> answerTexts;
    private List<String> answerBooleans;

    public MultipleChoiceQuestionDto() {
    }

    public MultipleChoiceQuestionDto(String questionText, int maxScore, String explanation, Map<String, Boolean> answers) {
        this.questionText = questionText;
        this.maxScore = maxScore;
        this.explanation = explanation;
        this.answers = answers;

        this.answerTexts = new ArrayList<>(answers.keySet());
        this.answerBooleans = answers.values().stream().map(String::valueOf).collect(Collectors.toList());
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

    public List<String> getAnswerTexts() {
        return answerTexts;
    }

    public void setAnswerTexts(List<String> answerTexts) {
        Set<String> uniqueAnswers = new HashSet<>(answerTexts);
        if (uniqueAnswers.size() != answerTexts.size()) {
            throw new DuplicateAnswerException("Antworten dürfen nicht mehrfach vorkommen");
        }

        this.answerTexts = answerTexts;
    }

    public Map<String, Boolean> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, Boolean> answers) {
        this.answers = answers;
    }

    public List<String> getAnswerBooleans() {
        return answerBooleans;
    }

    public void setAnswerBooleans(List<String> answerBooleans) {
        this.answerBooleans = answerBooleans;
    }

    public void parseAnswers() {
        if (answerTexts != null && answerBooleans != null) {
            this.answers = IntStream.range(0, answerTexts.size())
                    .boxed()
                    .collect(Collectors.toMap(answerTexts::get, i -> Boolean.parseBoolean(answerBooleans.get(i))));
        }
    }


    @Override
    public String toString() {
        return "MultipleChoiceQuestionDto{" +
                "questionText='" + questionText + '\'' +
                ", answerTexts=" + answerTexts +
                ", answerBooleans=" + answerBooleans +
                ", answersOptions=" + answers +
                ", maxScore=" + maxScore +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
