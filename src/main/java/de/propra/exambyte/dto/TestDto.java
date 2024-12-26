package de.propra.exambyte.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class TestDto {
    private String title;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime  startTime;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime endTime;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm") private LocalDateTime resultTime;


    public TestDto(String title, LocalDateTime  startTime, LocalDateTime endTime, LocalDateTime resultTime) {
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

    //Method to fill the time values and title with filler values. is just for testing and should eventually be removed
    public void fillTestValues() {
        this.title = "Test";
        this.startTime = LocalDateTime.now().plusHours(1);
        this.endTime = LocalDateTime.now().plusHours(2);
        this.resultTime = LocalDateTime.now().plusHours(3);
        System.out.println(this.toString());
    }


    @Override
    public String toString() {
        return "TestDto{" +
                "title='" + title + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", resultTime=" + resultTime +
                '}';
    }

}
