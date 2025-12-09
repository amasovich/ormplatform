package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Практическое задание (ASSIGNMENT).
 */
@Entity
@Table(name = "assignment")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ASSIGNMENT.lesson_id → LESSON.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    /** title в ER. */
    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore;

    /** SUBMISSION.assignment_id. */
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Submission> submissions = new ArrayList<>();

    public Assignment() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
}

