package ru.mifi.ormplatform.domain.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.mifi.ormplatform.domain.enums.QuestionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Вопрос квиза (QUESTION).
 */
@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** QUESTION.quiz_id → QUIZ.id. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(nullable = false, length = 2000)
    private String text;

    /**
     * type в ER (строка).
     * Enum маппится в строку.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private QuestionType type;

    /** ANSWEROPTION.question_id. */
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnswerOption> options = new ArrayList<>();

    public Question() {
    }

    // геттеры/сеттеры

    public Long getId() {
        return id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public String getText() {
        return text;
    }

    public QuestionType getType() {
        return type;
    }

    public List<AnswerOption> getOptions() {
        return options;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(QuestionType type) {
        this.type = type;
    }

    public void setOptions(List<AnswerOption> options) {
        this.options = options;
    }
}

