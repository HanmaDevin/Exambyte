package de.propra.exambyte.dto;

public class FreeTextQuestionDto {

    private String questionText;
    private int maxScore;
    private String possibleAnswer;

    public FreeTextQuestionDto() {
    }

    public FreeTextQuestionDto(String questionText, int maxScore, String possibleAnswer) {
        this.questionText = questionText;
        this.maxScore = maxScore;
        this.possibleAnswer = possibleAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
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

    @Override
    public String toString() {
        return "FreeTextQuestionDto{" +
                "questionText='" + questionText + '\'' +
                ", maxScore=" + maxScore +
                ", possibleAnswer='" + possibleAnswer + '\'' +
                '}';
    }
}
