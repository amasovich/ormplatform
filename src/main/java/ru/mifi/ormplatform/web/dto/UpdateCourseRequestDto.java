package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

/**
 * DTO для частичного обновления курса.
 * Все поля являются опциональными — обновляется только то, что пришло.
 */
public class UpdateCourseRequestDto {

    @Size(max = 255, message = "Название курса не должно превышать 255 символов")
    private String title;

    @Size(max = 2000, message = "Описание не должно превышать 2000 символов")
    private String description;

    /**
     * Новая продолжительность курса.
     * Может быть null (значит, поле не изменяется).
     */
    @Positive(message = "Продолжительность должна быть положительным числом")
    private Integer duration;

    @PastOrPresent(message = "Дата начала курса не может быть в будущем")
    private LocalDate startDate;

    /**
     * Нужен для корректной работы Jackson.
     */
    public UpdateCourseRequestDto() {
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
