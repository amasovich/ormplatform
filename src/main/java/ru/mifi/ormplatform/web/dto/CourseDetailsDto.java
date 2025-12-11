package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO, представляющий подробную структуру курса.
 *
 * Используется в:
 *   GET /api/courses/{id}
 *
 * Содержит общую информацию о курсе, список тегов,
 * а также полную иерархию модулей и уроков.
 */
public class CourseDetailsDto {

    /** Уникальный идентификатор курса */
    private Long id;

    /** Название курса */
    private String title;

    /** Описание курса */
    private String description;

    /** Название категории, к которой относится курс */
    private String categoryName;

    /** Имя преподавателя */
    private String teacherName;

    /** Продолжительность курса в часах */
    private Integer duration;

    /** Дата старта курса */
    private LocalDate startDate;

    /** Список тегов курса (только названия, без сущностей) */
    private List<String> tags;

    /** Полная структура модулей курса */
    private List<ModuleDto> modules;

    /**
     * Пустой конструктор необходим для корректной сериализации/десериализации JSON.
     */
    public CourseDetailsDto() {
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

    public List<ModuleDto> getModules() {
        return modules;
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

    public void setModules(List<ModuleDto> modules) {
        this.modules = modules;
    }
}
