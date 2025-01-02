package de.propra.exambyte.dto;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.stream.Collectors;


public class MultipleChoiceQuestionDto {

    private String questionText;
    private Map<String, Boolean> answers;
    private Integer maxScore;
    private String explanation;

    private List<String> answerTexts;
    private List<String> answerBooleans;

    public MultipleChoiceQuestionDto() {
    }

    public MultipleChoiceQuestionDto(String questionText, Integer maxScore, String explanation, Map<String, Boolean> answers) {
        this.questionText = questionText;
        this.maxScore = maxScore;
        this.explanation = explanation;
        this.answers = answers;
        //multimap is used as a workaround to store multiple values for a single key in order to properly use the exception handling
        Multimap<String, Boolean> multimap = convertToMultimap(answers);

        this.answerTexts = new ArrayList<>(multimap.keySet());
        this.answerBooleans = answers.values().stream().map(String::valueOf).collect(Collectors.toList());
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
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

    //TODO: handle duplicates in Service and don't convert to set, as it makes testing harder
    public void setAnswerTexts(List<String> answerTexts) {
        Set<String> uniqueAnswers = new HashSet<>(answerTexts);
        this.answerTexts = new ArrayList<>(uniqueAnswers);
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
            this.answers = new LinkedHashMap<>();
            for (int i = 0; i < answerTexts.size(); i++) {
                String key = answerTexts.get(i);
                Boolean value = Boolean.parseBoolean(answerBooleans.get(i));
                if (!this.answers.containsKey(key)) {
                    this.answers.put(key, value);
                }
            }
        }
    }

    public Multimap<String, Boolean> convertToMultimap(Map<String, Boolean> inputMap) {
        Multimap<String, Boolean> multimap = ArrayListMultimap.create();
        for (Map.Entry<String, Boolean> entry : inputMap.entrySet()) {
            multimap.put(entry.getKey(), entry.getValue());
        }
        return multimap;
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
