package ru.mifi.ormplatform.web.dto;

import java.time.LocalDateTime;

/**
 * DTO результата прохождения квиза студентом.
 * Этот объект отдаётся наружу — поэтому валидации здесь нет.
 */
public class QuizSubmissionDto {

    /** Уникальный идентификатор попытки. */
    private Long id;

    /** ID связанного квиза. */
    private Long quizId;

    /** Название квиза (для удобства фронтенда). */
    private String quizTitle;

    /** ID студента, который прошёл квиз. */
    private Long studentId;

    /** Имя студента (чтобы не делать повторный запрос на фронте). */
    private String studentName;

    /** Набранный балл. Может быть null, если квиз ещё не проверен вручную. */
    private Integer score;

    /** Дата и время прохождения. */
    private LocalDateTime takenAt;

    public QuizSubmissionDto() {
        // Пустой конструктор необходим для Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public LocalDateTime getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(LocalDateTime takenAt) {
        this.takenAt = takenAt;
    }
}
