package de.propra.exambyte.dto;

public class FreeTextQuestionDto {

    private String question;
    private int maxScore;
    private String possibleAnswer;

    public FreeTextQuestionDto() {
    }

    public FreeTextQuestionDto(String question, int maxScore, String possibleAnswer) {
        this.question = question;
        this.maxScore = maxScore;
        this.possibleAnswer = possibleAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public String getPossibleAnswer() {
        return possibleAnswer;
    }

    public void setPossibleAnswer(String possibleAnswer) {
        this.possibleAnswer = possibleAnswer;
    }
}
