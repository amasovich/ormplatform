package ru.mifi.ormplatform.web.dto;

/**
 * DTO-запрос для записи студента на курс.
 */
public class EnrollmentRequestDto {

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

