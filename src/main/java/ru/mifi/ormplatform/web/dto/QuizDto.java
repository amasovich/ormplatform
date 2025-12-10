package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * Полное представление квиза с вопросами и вариантами ответов.
 */
public class QuizDto {

    private Long id;
    private String title;
    /**
     * Лимит времени на прохождение квиза в минутах.
     */
    private Integer timeLimit;

    private Long courseId;
    private Long moduleId;

    private List<QuestionDto> questions;

    public QuizDto() {
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
