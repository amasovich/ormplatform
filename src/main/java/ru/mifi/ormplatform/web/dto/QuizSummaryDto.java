package ru.mifi.ormplatform.web.dto;

/**
 * Краткое представление квиза.
 * Использую для списков по модулю / курсу.
 */
public class QuizSummaryDto {

    private Long id;
    private Long courseId;
    private Long moduleId;
    private String title;

    /**
     * Лимит времени в минутах (может быть null, если не задан).
     */
    private Integer timeLimit;

    /**
     * Количество вопросов в квизе.
     */
    private Integer questionCount;

    /**
     * Максимально возможный балл за квиз.
     * (суммарное количество правильных вариантов по всем вопросам).
     */
    private Integer maxScore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }
}
