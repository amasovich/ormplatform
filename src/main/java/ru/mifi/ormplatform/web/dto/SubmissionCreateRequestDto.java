package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO-запрос для создания нового решения задания.
 * Используется в SubmissionController.
 *
 * Валидируются:
 *  - assignmentId — обязателен;
 *  - studentId — обязателен;
 *  - content — не пустой, ограничен по длине.
 */
public class SubmissionCreateRequestDto {

    @NotNull(message = "assignmentId обязательно")
    private Long assignmentId;

    @NotNull(message = "studentId обязательно")
    private Long studentId;

    @NotBlank(message = "Решение не может быть пустым")
    @Size(max = 5000, message = "Решение слишком большое (максимум 5000 символов)")
    private String content;

    public SubmissionCreateRequestDto() {
        // нужен для Jackson
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
