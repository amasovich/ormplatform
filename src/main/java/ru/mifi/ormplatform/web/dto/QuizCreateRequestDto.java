package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;

/**
 * DTO-запрос для создания нового квиза.
 *
 * Используется в:
 * POST /api/courses/{courseId}/modules/{moduleId}/quizzes
 */
public class QuizCreateRequestDto {

    /**
     * Название квиза.
     * Обязательно и должно быть не пустым.
     */
    @NotBlank(message = "Название квиза обязательно")
    @Size(max = 255, message = "Название квиза должно быть не длиннее 255 символов")
    private String title;

    /**
     * Лимит времени (в минутах).
     * Допускается null — тогда квиз без таймера.
     * Если указано значение, оно должно быть положительным.
     */
    @Positive(message = "Лимит времени должен быть положительным числом")
    private Integer timeLimit;

    public QuizCreateRequestDto() {
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
