package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO для создания нового курса.
 * Duration — Integer, как в модели данных.
 */
public class CreateCourseRequestDto {

    @NotBlank(message = "Название курса обязательно")
    @Size(max = 255, message = "Название курса не должно превышать 255 символов")
    private String title;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Преподаватель обязателен")
    private Long teacherId;

    /**
     * Продолжительность курса в часах.
     */
    @NotNull(message = "Продолжительность курса обязательна")
    @Positive(message = "Продолжительность должна быть положительным числом")
    private Integer duration;

    @PastOrPresent(message = "Дата начала курса не может быть в будущем")
    private LocalDate startDate;

    /**
     * Пустой конструктор обязателен для Jackson.
     */
    public CreateCourseRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ====== GETTERS & SETTERS ======

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
