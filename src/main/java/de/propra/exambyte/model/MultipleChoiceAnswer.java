package de.propra.exambyte.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class MultipleChoiceAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    private Set<String> selectedAnswers;

    @OneToOne
    private MultipleChoiceQuestion multipleChoiceQuestion;

    public MultipleChoiceAnswer() {}

    public MultipleChoiceAnswer(Set<String> selectedAnswers, MultipleChoiceQuestion multipleChoiceQuestion) {
        this.selectedAnswers = selectedAnswers;
        this.multipleChoiceQuestion = multipleChoiceQuestion;
    }

    public Long getId() {
        return id;
    }

    public Set<String> getSelectedAnswers() {
        return selectedAnswers;
    }

    public void setSelectedAnswers(Set<String> selectedAnswers) {
        this.selectedAnswers = selectedAnswers;
    }

    public MultipleChoiceQuestion getMultipleChoiceQuestion() {
        return multipleChoiceQuestion;
    }

    public void setMultipleChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion) {
        this.multipleChoiceQuestion = multipleChoiceQuestion;
    }
}
