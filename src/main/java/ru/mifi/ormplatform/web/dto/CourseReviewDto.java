package ru.mifi.ormplatform.web.dto;

import java.time.LocalDateTime;

/**
 * DTO для отображения отзыва о курсе.
 * Содержит только безопасную и необходимую информацию.
 */
public class CourseReviewDto {

    /** Идентификатор отзыва */
    private Long id;

    /** Курс, к которому относится отзыв */
    private Long courseId;
    private String courseTitle;

    /** Информация о студенте */
    private Long studentId;
    private String studentName;

    /**
     * Рейтинг курса (1–5).
     * Валидация выполняется на уровне входящих DTO (create/update),
     * здесь отдаётся уже сохранённое значение.
     */
    private Integer rating;

    /** Текст комментария студента */
    private String comment;

    /** Дата и время создания отзыва */
    private LocalDateTime createdAt;

    public CourseReviewDto() {
        // Пустой конструктор необходим для Jackson
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
