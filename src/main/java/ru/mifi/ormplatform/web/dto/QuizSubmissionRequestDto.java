package ru.mifi.ormplatform.web.dto;

import java.util.Map;

/**
 * Запрос на отправку ответов студента на квиз.
 *
 * Ключ в карте answers — идентификатор вопроса,
 * значение — идентификатор выбранного варианта ответа.
 */
public class QuizSubmissionRequestDto {

    private Long studentId;
    private Map<Long, Long> answers;

    public QuizSubmissionRequestDto() {
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
