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
