package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;

/**
 * DTO для обновления существующего курса.
 */
public class UpdateCourseRequestDto {

    private String title;
    private String description;
    private String duration;
    private LocalDate startDate;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
