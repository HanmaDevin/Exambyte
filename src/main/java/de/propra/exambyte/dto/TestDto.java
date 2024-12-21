package de.propra.exambyte.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TestDto {
    private String title;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime  startTime;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime endTime;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime resultTime;


    public TestDto(String title, @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime  startTime, @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime endTime,  @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm")LocalDateTime resultTime) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.resultTime = resultTime;
    }

    public TestDto() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getResultTime() {
        return resultTime;
    }

    public void setResultTime(LocalDateTime resultTime) {
        this.resultTime = resultTime;
    }
}
