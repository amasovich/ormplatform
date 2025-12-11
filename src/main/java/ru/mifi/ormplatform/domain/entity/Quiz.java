package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Квиз (QUIZ).
 */
@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** QUIZ.module_id → MODULE.id. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false, unique = true)
    private Module module;

    @Column(nullable = false, length = 200)
    private String title;

    /** timeLimit (минуты) в ER. */
    @Column(name = "time_limit")
    private Integer timeLimit;

    /** QUESTION.quiz_id. */
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Question> questions = new ArrayList<>();

    /** QUIZSUBMISSION.quiz_id. */
    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<QuizSubmission> submissions = new ArrayList<>();

    public Quiz() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Module getModule() {
        return module;
    }


    public String getTitle() {
        return title;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<QuizSubmission> getSubmissions() {
        return submissions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setSubmissions(List<QuizSubmission> submissions) {
        this.submissions = submissions;
    }
}

