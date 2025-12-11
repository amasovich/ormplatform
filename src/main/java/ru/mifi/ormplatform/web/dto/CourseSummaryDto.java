package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * Краткое представление курса.
 *
 * Используется в списках:
 *   - GET /api/courses
 *   - GET /api/courses/search
 *   - GET /api/courses/by-category/{id}
 *   - GET /api/courses/by-teacher/{id}
 *
 * Содержит только базовые данные — без структуры модулей.
 */
public class CourseSummaryDto {

    /** Идентификатор курса */
    private Long id;

    /** Название курса */
    private String title;

    /** Краткое описание курса */
    private String description;

    /** Название категории */
    private String categoryName;

    /** Имя преподавателя */
    private String teacherName;

    /** Продолжительность курса в часах */
    private Integer duration;

    /** Дата старта курса */
    private LocalDate startDate;

    /** Список тегов курса (только названия) */
    private List<String> tags;

    /**
     * Пустой конструктор — требуется Jackson.
     */
    public CourseSummaryDto() {
        // Пустой конструктор необходим для Jackson
    }

    // =========================
    //         GETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public Integer getDuration() {
        return duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<String> getTags() {
        return tags;
    }

    // =========================
    //         SETTERS
    // =========================

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
