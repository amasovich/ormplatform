package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO для обновления существующего отзыва.
 * Обновляем только рейтинг и комментарий — оба поля опциональны.
 */
public class CourseReviewUpdateDto {

    /**
     * Новый рейтинг.
     * Может быть null — в этом случае рейтинг не изменяется.
     */
    @Min(value = 1, message = "Рейтинг должен быть не ниже 1")
    @Max(value = 5, message = "Рейтинг должен быть не выше 5")
    private Integer rating;

    /**
     * Новый комментарий.
     * Может быть null — комментарий не изменяется.
     */
    @Size(max = 1000, message = "Комментарий не может превышать 1000 символов")
    private String comment;

    public CourseReviewUpdateDto() {
        // Пустой конструктор необходим для Jackson
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
