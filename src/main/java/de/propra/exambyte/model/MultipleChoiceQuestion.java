package de.propra.exambyte.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class MultipleChoiceQuestion implements Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;

    @ElementCollection
    private List<String> answersOptions;

    @ElementCollection
    private List<Integer> correctAnswers;

    private int maxScore;

    private String explanation;

    @ManyToOne
    private Test test;

    public MultipleChoiceQuestion() {

    }

    public MultipleChoiceQuestion(Long id, String questionText, List<String> answersOptions, List<Integer> correctAnswers, int maxScore, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.answersOptions = answersOptions;
        this.correctAnswers = correctAnswers;
        this.maxScore = maxScore;
        this.explanation = explanation;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getQuestion() {
        return questionText;
    }

    @Override
    public int getMaxScore() {
        return maxScore;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
