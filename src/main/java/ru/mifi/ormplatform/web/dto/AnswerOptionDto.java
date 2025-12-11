package ru.mifi.ormplatform.web.dto;

/**
 * DTO варианта ответа в вопросе квиза.
 *
 * Используется только для вывода наружу.
 * Валидация здесь не нужна, так как клиент этот DTO не отправляет.
 */
public class AnswerOptionDto {

    private Long id;

    /**
     * Текст варианта ответа.
     */
    private String text;

    /**
     * Признак правильного ответа.
     */
    private boolean correct;

    public AnswerOptionDto() {
        // пустой конструктор для Jackson
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

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
