package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO-запрос для записи студента на курс.
 * Используется в POST /api/courses/{courseId}/enroll
 */
public class EnrollmentRequestDto {

    /**
     * Идентификатор студента, который записывается на курс.
     */
    @NotNull(message = "studentId обязателен")
    private Long studentId;

    public EnrollmentRequestDto() {
        // нужен для Jackson
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
