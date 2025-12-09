package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

import java.time.LocalDate;

/**
 * Запись студента на курс (ENROLLMENT).
 */
@Entity
@Table(name = "enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ENROLLMENT.user_id → USER.id (студент). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User student;

    /** ENROLLMENT.course_id → COURSE.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /** enrollDate в ER. */
    @Column(name = "enroll_date", nullable = false)
    private LocalDate enrollDate;

    /**
     * status в ER (строка).
     * Мапплю enum в строку.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private EnrollmentStatus status;

    public Enrollment() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getEnrollDate() {
        return enrollDate;
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setEnrollDate(LocalDate enrollDate) {
        this.enrollDate = enrollDate;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}

