package de.propra.exambyte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class FreeTextQuestion implements Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;
    private Integer maxScore;
    private String possibleAnswer;

    @OneToOne
    private FreeTextAnswer freeTextAnswer;

    @ManyToOne
    private Test test;

    public FreeTextQuestion() {
    }

    public FreeTextQuestion(String questionText, String possibleAnswer, int maxScore) {
        this.questionText = questionText;
        this.possibleAnswer = possibleAnswer;
        this.maxScore = maxScore;
    }


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getQuestionText() {
        return questionText;
    }

    @Override
    public Integer getMaxScore() {
        return maxScore;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    @Override
    public String getType() {
        return "FreeTextQuestion";
    }

    public void setFreeTextAnswer(FreeTextAnswer freeTextAnswer) {
        this.freeTextAnswer = freeTextAnswer;
    }

    public void updateQuestion(String questionText, String possibleAnswer, int maxScore) {
        this.questionText = questionText;
        this.possibleAnswer = possibleAnswer;
        this.maxScore = maxScore;
    }

    @Override
    public String toString() {
        return "FreeTextQuestion{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", maxScore=" + maxScore +
                ", possibleAnswer='" + possibleAnswer + '\'' +
                '}';
    }
}
