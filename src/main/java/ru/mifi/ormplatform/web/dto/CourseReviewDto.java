package ru.mifi.ormplatform.web.dto;

import java.time.LocalDateTime;

/**
 * DTO для отображения отзыва о курсе.
 * Передаётся наружу только безопасная информация:
 * студент, курс, рейтинг и текст отзыва.
 */
public class CourseReviewDto {

    private Long id;
    private Long courseId;
    private String courseTitle;

    private Long studentId;
    private String studentName;

    private Integer rating;
    private String comment;

    private LocalDateTime createdAt;

    public CourseReviewDto() {
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

