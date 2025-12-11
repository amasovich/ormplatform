package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * DTO полного представления квиза.
 * <p>
 * Используется при:
 * — получении квиза целиком,
 * — отображении вопросов и вариантов ответов,
 * — подготовке данных для прохождения теста.
 * <p>
 * Включает в себя:
 * - общие метаданные квиза,
 * - связь с курсом и модулем,
 * - список вопросов с вариантами ответов.
 */
public class QuizDto {

    /** Уникальный идентификатор квиза. */
    private Long id;

    /** Название квиза. */
    private String title;

    /**
     * Ограничение по времени в минутах.
     * Может быть null (тогда квиз без таймера).
     */
    private Integer timeLimit;

    /** Идентификатор курса, к которому привязан квиз. */
    private Long courseId;

    /** Идентификатор модуля курса. */
    private Long moduleId;

    /** Список вопросов квиза со всеми вариантами ответов. */
    private List<QuestionDto> questions;

    public QuizDto() {
        // Пустой конструктор необходим для Jackson
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }
}
