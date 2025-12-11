package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Size;
import ru.mifi.ormplatform.domain.enums.QuestionType;

/**
 * DTO для частичного обновления существующего вопроса квиза.
 * <p>
 * Все поля необязательные:
 * - если text = null → текст не изменяем;
 * - если type = null → тип не изменяем.
 */
public class QuestionUpdateRequestDto {

    /**
     * Новый текст вопроса.
     * Может быть null (в этом случае оставляем старое значение).
     */
    @Size(max = 500, message = "Текст вопроса должен быть не длиннее 500 символов")
    private String text;

    /**
     * Новый тип вопроса:
     * SINGLE_CHOICE / MULTI_CHOICE / TEXT.
     * Может быть null (значит, не обновляем тип).
     */
    private QuestionType type;

    public QuestionUpdateRequestDto() {
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
