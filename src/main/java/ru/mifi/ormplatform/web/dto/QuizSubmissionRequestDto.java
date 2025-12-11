package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.*;
import java.util.Map;

/**
 * Запрос на отправку ответов студента на квиз.
 * answers:
 *   ключ — ID вопроса,
 *   значение — ID выбранного варианта ответа.
 */
public class QuizSubmissionRequestDto {

    @NotNull(message = "studentId обязателен")
    @Positive(message = "studentId должен быть положительным числом")
    private Long studentId;

    /**
     * Карта: questionId -> answerOptionId.
     * Нельзя прислать пустой или null словарь.
     */
    @NotNull(message = "Список ответов обязателен")
    @Size(min = 1, message = "Должен быть указан хотя бы один ответ")
    private Map<
            @NotNull(message = "questionId не может быть null")
            @Positive(message = "questionId должен быть положительным числом")
                    Long,

            @NotNull(message = "answerOptionId не может быть null")
            @Positive(message = "answerOptionId должен быть положительным числом")
                    Long
            > answers;

    public QuizSubmissionRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Map<Long, Long> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, Long> answers) {
        this.answers = answers;
    }
}
