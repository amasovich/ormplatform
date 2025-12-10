package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Краткое представление курса.
 * Я использую этот DTO для списков курсов, где не нужна полная структура.
 */
public class CourseSummaryDto {

    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private String teacherName;
    private String duration;
    private LocalDate startDate;
    private List<String> tags;

    public CourseSummaryDto() {
        // пустой конструктор нужен Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
