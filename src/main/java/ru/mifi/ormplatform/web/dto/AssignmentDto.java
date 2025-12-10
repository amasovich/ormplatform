package ru.mifi.ormplatform.web.dto;

import java.time.LocalDate;

/**
 * DTO для отображения задания по уроку.
 * <p>
 * Здесь я отдаю наружу только ту информацию, которая нужна клиенту:
 * id, к какому уроку относится задание, текст, дедлайн и максимальный балл.
 */
public class AssignmentDto {

    private Long id;
    private Long lessonId;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Integer maxScore;

    public AssignmentDto() {
        // Нужен пустой конструктор для Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
}