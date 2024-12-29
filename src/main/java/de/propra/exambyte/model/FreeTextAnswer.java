package de.propra.exambyte.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class FreeTextAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Answer is mandatory")
    private String answer;

    @NotBlank(message = "Score is mandatory")
    private int score;

    @NotBlank(message = "Corrector Comment is mandatory")
    private String correctorComment;

    @OneToOne
    private FreeTextQuestion freeTextQuestion;

    public FreeTextAnswer() {
    }

    public FreeTextAnswer(String answer, int score) {
        this.answer = answer;
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setCorrectorComment(String correctorComment) {
        this.correctorComment = correctorComment;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
