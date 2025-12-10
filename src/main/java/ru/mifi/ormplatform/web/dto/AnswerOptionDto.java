package ru.mifi.ormplatform.web.dto;

/**
 * Вариант ответа для вопроса квиза.
 * В учебных целях я отдаю только id и текст,
 * без признака правильности.
 */
public class AnswerOptionDto {

    private Long id;
    private String text;

    public AnswerOptionDto() {
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
}
