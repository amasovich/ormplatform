package ru.mifi.ormplatform.web.dto;

/**
 * DTO для оценки решения студентом.
 * <p>
 * Используется преподавателем для выставления оценки и комментария.
 * Пример запроса:
 * {
 *   "score": 85,
 *   "feedback": "Хорошее решение, но можно улучшить форматирование кода."
 * }
 */
public class SubmissionGradeDto {

    private Integer score;
    private String feedback;

    public SubmissionGradeDto() {
        // Пустой конструктор для Jackson
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
