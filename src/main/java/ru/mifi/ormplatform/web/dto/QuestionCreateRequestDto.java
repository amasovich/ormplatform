package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.mifi.ormplatform.domain.enums.QuestionType;

/**
 * DTO для создания нового вопроса квиза.
 * Используется в POST /api/quizzes/{quizId}/questions.
 *
 * Валидация:
 * - text обязателен и ограничен по длине;
 * - type обязателен (SINGLE_CHOICE, MULTI_CHOICE или TEXT).
 */
public class QuestionCreateRequestDto {

    @NotBlank(message = "Текст вопроса обязателен")
    @Size(max = 500, message = "Текст вопроса должен быть не длиннее 500 символов")
    private String text;

    @NotNull(message = "Тип вопроса обязателен")
    private QuestionType type;

    public QuestionCreateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }
}
