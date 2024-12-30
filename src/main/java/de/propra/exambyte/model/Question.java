package de.propra.exambyte.model;

public interface Question {
    Long getId();
    String getQuestionText();
    Integer getMaxScore();
    String toString();
    String getType();
}
