package de.propra.exambyte.dto;

public class FreeTextAnswerDto {

    private String answer;
    private int score;

    public FreeTextAnswerDto() {
    }

    public FreeTextAnswerDto(String answer, int score) {
        this.answer = answer;
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "FreeTextAnswerDto{" +
                "answer='" + answer + '\'' +
                ", score=" + score +
                '}';
    }
}
