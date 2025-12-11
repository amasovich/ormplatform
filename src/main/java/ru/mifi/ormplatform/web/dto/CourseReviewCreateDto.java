package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для создания нового отзыва о курсе.
 * Студент может отправить только рейтинг и комментарий.
 * Валидация предотвращает отправку некорректных данных.
 */
public class CourseReviewCreateDto {

    @NotNull(message = "ID студента обязателен")
    private Long studentId;

    @NotNull(message = "Рейтинг обязателен")
    @Min(value = 1, message = "Рейтинг должен быть не ниже 1")
    @Max(value = 5, message = "Рейтинг должен быть не выше 5")
    private Integer rating;

    @Size(max = 1000, message = "Комментарий не может превышать 1000 символов")
    private String comment;

    public CourseReviewCreateDto() {
        // Пустой конструктор необходим для Jackson
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
