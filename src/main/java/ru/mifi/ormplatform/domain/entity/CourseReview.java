package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Отзыв студента о курсе (COURSEREVIEW).
 */
@Entity
@Table(name = "course_review")
public class CourseReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** COURSEREVIEW.course_id → COURSE.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /** COURSEREVIEW.student_id → USER.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 2000)
    private String comment;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CourseReview() {
    }

    // геттеры/сеттеры


    public Long getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public User getStudent() {
        return student;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

