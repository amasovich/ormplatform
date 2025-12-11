package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO-запрос, который отправляет студент при сдаче задания.
 * Здесь передаётся только studentId и содержание решения.
 *
 * Используется в:
 * POST /api/assignments/{assignmentId}/submissions
 */
public class SubmissionRequestDto {

    @NotNull(message = "studentId обязателен")
    private Long studentId;

    @NotBlank(message = "Решение не может быть пустым")
    @Size(max = 5000, message = "Содержание решения слишком большое (максимум 5000 символов)")
    private String content;

    public SubmissionRequestDto() {
        // Нужен пустой конструктор для Jackson
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
