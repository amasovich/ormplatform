package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.QuestionType;
import java.util.List;

/**
 * DTO для отображения вопроса квиза.
 * <p>
 * Используется в:
 * - получении квиза целиком,
 * - отображении вопроса вместе с вариантами ответа.
 * <p>
 * Все поля предназначены только для чтения клиентом.
 */
public class QuestionDto {

    private Long id;

    /** Текст вопроса. */
    private String text;

    /**
     * Тип вопроса:
     * SINGLE_CHOICE — один правильный ответ,
     * MULTIPLE_CHOICE — несколько правильных,
     * TEXT — текстовый ввод пользователя.
     */
    private QuestionType type;

    /** Варианты ответа (только для SINGLE/MULTIPLE CHOICE). */
    private List<AnswerOptionDto> options;

    public QuestionDto() {
        // Пустой конструктор необходим для Jackson
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
