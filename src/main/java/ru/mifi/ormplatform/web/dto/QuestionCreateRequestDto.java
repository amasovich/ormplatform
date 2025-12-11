package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.QuestionType;

/**
 * DTO для создания нового вопроса квиза.
 */
public class QuestionCreateRequestDto {

    private String text;
    private QuestionType type;

    public QuestionCreateRequestDto() {
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
