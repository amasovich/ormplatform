package ru.mifi.ormplatform.web.dto;

/**
 * DTO для обновления варианта ответа.
 */
public class AnswerOptionUpdateRequestDto {

    private String text;
    private boolean correct;

    public AnswerOptionUpdateRequestDto() {
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
