package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;

/**
 * Вариант ответа на вопрос (ANSWEROPTION).
 */
@Entity
@Table(name = "answer_option")
public class AnswerOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ANSWEROPTION.question_id → QUESTION.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(nullable = false, length = 1000)
    private String text;

    /** isCorrect в ER. */
    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    public AnswerOption() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Question getQuestion() {
        return question;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

