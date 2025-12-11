package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO для отображения задания по уроку.
 *
 * Передаёт наружу только необходимые поля:
 *  - id задания
 *  - id урока
 *  - заголовок
 *  - описание
 *  - дедлайн
 *  - максимальный балл
 *
 * Используется в AssignmentController.
 */
public class AssignmentDto {

    /** Идентификатор задания */
    private Long id;

    /** Идентификатор урока, к которому относится задание */
    private Long lessonId;

    /** Название задания */
    private String title;

    /** Описание задания */
    private String description;

    /** Дата дедлайна (может быть null) */
    private LocalDate dueDate;

    /** Максимальный балл за выполнение */
    private Integer maxScore;

    public AssignmentDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setId(Long id) {
        this.id = id;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
}
