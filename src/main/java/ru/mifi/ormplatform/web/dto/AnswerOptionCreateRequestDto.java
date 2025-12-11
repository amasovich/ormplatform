package ru.mifi.ormplatform.web.dto;

/**
 * DTO для создания варианта ответа.
 */
public class AnswerOptionCreateRequestDto {

    private String text;
    private boolean correct;

    public AnswerOptionCreateRequestDto() {
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
