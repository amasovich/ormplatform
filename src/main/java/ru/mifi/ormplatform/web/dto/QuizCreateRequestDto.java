package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос для создания нового квиза.
 * <p>
 * Используется в POST /api/courses/{courseId}/modules/{moduleId}/quizzes
 */
public class QuizCreateRequestDto {

    /**
     * Название квиза.
     */
    private String title;

    /**
     * Лимит времени на прохождение квиза (в минутах).
     * Может быть null — тогда не ограничено.
     */
    private Integer timeLimit;

    public QuizCreateRequestDto() {
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
