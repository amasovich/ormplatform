package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос для создания нового решения задания.
 * Используется в SubmissionController.
 */
public class SubmissionCreateRequestDto {

    private Long assignmentId;
    private Long studentId;
    private String content;

    public SubmissionCreateRequestDto() {
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
