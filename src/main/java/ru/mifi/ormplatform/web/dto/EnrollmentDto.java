package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

import java.time.LocalDate;

/**
 * DTO для представления записи студента на курс.
 * Это DTO-ответ, поэтому аннотации валидации не используются.
 */
public class EnrollmentDto {

    private Long id;

    private Long courseId;
    private String courseTitle;

    private Long studentId;
    private String studentName;

    private EnrollmentStatus status;

    private LocalDate enrollDate;

    public EnrollmentDto() {
        // Пустой конструктор нужен Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
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

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = EnrollmentStatus.valueOf(status);
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }
}
