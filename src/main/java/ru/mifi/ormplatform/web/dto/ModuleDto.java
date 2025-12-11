package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * DTO модуля курса.
 *
 * Используется:
 *  - в CourseDetailsDto (полная структура курса),
 *  - в CourseSummaryDto опционально,
 *  - в ModuleController для отдачи данных наружу.
 *
 * Это DTO — только для чтения (read-only).
 * Для создания и обновления используются:
 *  - ModuleCreateRequestDto
 *  - ModuleUpdateRequestDto
 */
public class ModuleDto {

    /** ID модуля */
    private Long id;

    /** Название модуля */
    private String title;

    /** Описание модуля */
    private String description;

    /** Порядковый номер в рамках курса */
    private Integer orderIndex;

    /** Список уроков модуля */
    private List<LessonDto> lessons;

    /**
     * Пустой конструктор — требуется Jackson.
     */
    public ModuleDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }
}
