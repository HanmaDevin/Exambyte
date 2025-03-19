package de.propra.exambyte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class FreeTextAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Answer is mandatory")
    @Lob
    private String studentAnswer;


    @OneToOne
    private FreeTextQuestion freeTextQuestion;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Lob
    private String feedback;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    private Integer score;

    public FreeTextAnswer() {
    }

    public FreeTextAnswer(String studentAnswer, FreeTextQuestion freeTextQuestion) {
        this.studentAnswer = studentAnswer;
        this.freeTextQuestion = freeTextQuestion;
    }

    public Long getId() {
        return id;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }
    
    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public FreeTextQuestion getFreeTextQuestion() {
        return freeTextQuestion;
    }

    public void setFreeTextQuestion(FreeTextQuestion freeTextQuestion) {
        this.freeTextQuestion = freeTextQuestion;
    }
}
