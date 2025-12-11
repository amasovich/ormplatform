package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO-запрос для обновления существующего квиза.
 * Используется в PUT /api/quizzes/{id}.
 *
 * Все поля НЕ обязательны.
 * Но если они переданы — должны проходить валидацию.
 */
public class QuizUpdateRequestDto {

    /**
     * Новое название квиза.
     * Может быть null — тогда название не меняется.
     */
    @Size(min = 3, max = 255, message = "Название квиза должно содержать от 3 до 255 символов")
    private String title;

    /**
     * Новый лимит времени (может быть null).
     * Если задан → только положительное число.
     */
    @Positive(message = "Лимит времени должен быть положительным числом")
    private Integer timeLimit;

    public QuizUpdateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
