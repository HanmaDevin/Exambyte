package de.propra.exambyte.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestDtoTest {

    @Test
    @DisplayName("TestDto Konstruktor und Getters funktionieren wie erwartet")
    public void test1() {
        String title = "Test";
        TestDto dto = new TestDto(title, null, null, null);
        assertEquals(title, dto.getTitle());
        assertNull(dto.getStartTime());
        assertNull(dto.getEndTime());
        assertNull(dto.getResultTime());
    }

    @Test
    @DisplayName("TestDto Setter funktionieren wie erwartet")
    public void test2() {
        String title = "Test";
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 13, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 1, 1, 14, 0);

        TestDto dto = new TestDto();
        dto.setTitle(title);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setResultTime(resultTime);

        assertEquals(title, dto.getTitle());
        assertEquals(startTime, dto.getStartTime());
        assertEquals(endTime, dto.getEndTime());
        assertEquals(resultTime, dto.getResultTime());
    }

    @Test
    @DisplayName("toString() Methode von TestDto funktioniert wie erwartet")
    public void test3() {
        String title = "Test";
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 1, 13, 0);
        LocalDateTime resultTime = LocalDateTime.of(2025, 1, 1, 14, 0);

        TestDto dto = new TestDto(title, startTime, endTime, resultTime);

        String expected = "TestDto{title='Test', startTime=2025-01-01T12:00, endTime=2025-01-01T13:00, resultTime=2025-01-01T14:00}";
        assertEquals(expected, dto.toString());
    }
}
