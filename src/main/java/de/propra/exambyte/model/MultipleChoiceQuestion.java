package de.propra.exambyte.model;

import jakarta.persistence.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class MultipleChoiceQuestion implements Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;

    @ElementCollection
    private Map<String, Boolean> answers;

    private Integer maxScore;

    private String explanation;

    @OneToOne
    private MultipleChoiceAnswer multipleChoiceAnswer;

    @ManyToOne
    private Test test;

    public MultipleChoiceQuestion() {

    }

    public MultipleChoiceQuestion(String questionText, Integer maxScore, String explanation, Map<String, Boolean> answers) {
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
    public Integer getMaxScore() {
        return maxScore;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    @Override
    public String getType() {
        return "MultipleChoiceQuestion";
    }

    public void updateQuestion(String questionText, Integer maxScore, String explanation, Map<String, Boolean> answers) {
        this.questionText = questionText;


        for (Map.Entry<String, Boolean> entry : answers.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                this.answers.put(entry.getKey(), entry.getValue());
            }
        }

        this.maxScore = maxScore;
        this.explanation = explanation;
    }

    public Set<String> getCorrectAnswers() {
        return answers.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
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

    public MultipleChoiceAnswer getMultipleChoiceAnswer() {
        return multipleChoiceAnswer;
    }

    public void setMultipleChoiceAnswer(MultipleChoiceAnswer multipleChoiceAnswer) {
        this.multipleChoiceAnswer = multipleChoiceAnswer;
    }
}
