package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.QuestionType;
import java.util.List;

/**
 * Представление вопроса квиза для REST.
 */
public class QuestionDto {

    private Long id;
    private String text;

    /**
     * Тип вопроса — enum (SINGLE_CHOICE, MULTIPLE_CHOICE, TEXT)
     */
    private QuestionType type;

    private List<AnswerOptionDto> options;

    public QuestionDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<AnswerOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDto> options) {
        this.options = options;
    }
}
