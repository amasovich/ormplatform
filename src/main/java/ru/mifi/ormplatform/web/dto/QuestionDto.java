package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * Представление вопроса квиза для REST-слоя.
 */
public class QuestionDto {

    private Long id;
    private String text;
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AnswerOptionDto> getOptions() {
        return options;
    }

    public void setOptions(List<AnswerOptionDto> options) {
        this.options = options;
    }
}
