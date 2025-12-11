package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для создания варианта ответа в вопросе квиза.
 * <p>
 * Используется в POST/PUT запросах внутри QuizController.
 * Обязательные поля:
 * - text — не пустой, ограничение по длине.
 * - correct — булевый флаг правильности.
 */
public class AnswerOptionCreateRequestDto {

    @NotBlank(message = "Текст варианта ответа не может быть пустым")
    @Size(max = 300, message = "Текст варианта ответа должен быть не длиннее 300 символов")
    private String text;

    /**
     * Признак правильного ответа.
     * Может быть false — это нормально.
     */
    private boolean correct;

    public AnswerOptionCreateRequestDto() {
        // пустой конструктор для Jackson
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
