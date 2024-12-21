package de.propra.exambyte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
public class FreeTextQuestion implements Questions {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String questionText;
    private int maxScore;

    @ManyToOne
    private Test test;

    public FreeTextQuestion() {
    }

    public FreeTextQuestion(Long id, String questionText, int maxScore) {
        this.id = id;
        this.questionText = questionText;
        this.maxScore = maxScore;
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
