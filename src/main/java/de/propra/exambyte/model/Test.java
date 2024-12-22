package de.propra.exambyte.model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    private String title;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FreeTextQuestion> freeTextQuestions = new ArrayList<>();


    @NotNull(message = "Start time is mandatory")
    private LocalDateTime startTime;
    @NotNull(message = "End time is mandatory")
    private LocalDateTime endTime;
    @NotNull(message = "Result time is mandatory")
    private LocalDateTime resultTime;

    public Test() {
    }

    public Test(String title, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime resultTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
    }

    public LocalDateTime getResultTime() {
        return resultTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public boolean isTestInProgress() {
        return LocalDateTime.now().isAfter(startTime) && LocalDateTime.now().isBefore(endTime);
    }

    public boolean isResultAvailable() {
        return LocalDateTime.now().isAfter(endTime);
    }

    public void addMultipleChoiceQuestion(MultipleChoiceQuestion question) {
        question.setTest(this);
        multipleChoiceQuestions.add(question);
    }

    public void addFreeTextQuestion(FreeTextQuestion question) {
        question.setTest(this);
        freeTextQuestions.add(question);
    }

    public List<Questions> getMultipleChoiceQuestions() {
        return new ArrayList<>(multipleChoiceQuestions);
    }

    public List<Questions> getFreeTextQuestions() {
        return new ArrayList<>(freeTextQuestions);
    }

    public List<Questions> getAllQuestions() {
        List<Questions> questions = new ArrayList<>();
        questions.addAll(multipleChoiceQuestions);
        questions.addAll(freeTextQuestions);
        return questions;
    }

}
