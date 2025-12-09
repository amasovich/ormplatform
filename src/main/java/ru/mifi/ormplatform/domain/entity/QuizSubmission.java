package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Результат прохождения квиза (QUIZSUBMISSION).
 */
@Entity
@Table(name = "quiz_submission")
public class QuizSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** QUIZSUBMISSION.quiz_id → QUIZ.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    /** QUIZSUBMISSION.student_id → USER.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    private Integer score;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt;

    public QuizSubmission() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public User getStudent() {
        return student;
    }

    public Integer getScore() {
        return score;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }
}

