package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос для обновления существующего квиза.
 * <p>
 * Используется в PUT /api/quizzes/{id}
 */
public class QuizUpdateRequestDto {

    /**
     * Новое название квиза.
     */
    private String title;

    /**
     * Новый лимит времени (может быть null).
     */
    private Integer timeLimit;

    public QuizUpdateRequestDto() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
}
