package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * DTO для оценки решения студента преподавателем.
 *
 * Пример запроса:
 * {
 *   "score": 85,
 *   "feedback": "Хорошее решение!"
 * }
 */
public class SubmissionGradeDto {

    /**
     * Оценка за решение (0–100).
     * Может быть null, если преподаватель оставляет только комментарий.
     */
    @Min(value = 0, message = "Оценка не может быть меньше 0")
    @Max(value = 100, message = "Оценка не может быть больше 100")
    private Integer score;

    /**
     * Комментарий преподавателя.
     * Может быть пустым, но ограничен по длине.
     */
    @Size(max = 2000, message = "Комментарий слишком длинный (максимум 2000 символов)")
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
