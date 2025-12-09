package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Решение задания (SUBMISSION).
 */
@Entity
@Table(name = "submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** SUBMISSION.assignment_id → ASSIGNMENT.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    /** SUBMISSION.student_id → USER.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Integer score;

    @Column(length = 2000)
    private String feedback;

    public Submission() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public User getStudent() {
        return student;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getContent() {
        return content;
    }

    public Integer getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}

