package ru.mifi.ormplatform.web.dto;

import java.time.LocalDateTime;

/**
 * DTO для отображения отправленного решения по заданию.
 *
 * Содержит:
 *  - информацию о студенте,
 *  - информацию о задании,
 *  - дату отправки,
 *  - результат проверки (оценку и комментарий).
 *
 * Используется только для чтения (response).
 */
public class SubmissionDto {

    private Long id;

    private Long assignmentId;
    private String assignmentTitle;

    private Long studentId;
    private String studentName;

    private LocalDateTime submittedAt;

    /** Сдаваемый текст/ссылка */
    private String content;

    /** Может быть null, если преподаватель ещё не проверил */
    private Integer score;

    /** Комментарий ментора */
    private String feedback;

    public SubmissionDto() {
        // Пустой конструктор для Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
