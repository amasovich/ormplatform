package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;

/**
 * DTO для создания нового курса.
 */
public class CreateCourseRequestDto {

    private String title;
    private String description;
    private Long categoryId;
    private Long teacherId;
    private String duration;
    private LocalDate startDate;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public String getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
