package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO для обновления варианта ответа в вопросе квиза.
 * <p>
 * Все поля опциональны — можно обновлять частично.
 * Если фронт передаёт null, поле не изменяется.
 */
public class AnswerOptionUpdateRequestDto {

    @Size(max = 300, message = "Текст варианта ответа должен быть не длиннее 300 символов")
    private String text;

    /**
     * Значение может быть true или false.
     * Если понадобятся именно частичные обновления, можно заменить Boolean correct — но пока оставляем boolean.
     */
    private boolean correct;

    public AnswerOptionUpdateRequestDto() {
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
