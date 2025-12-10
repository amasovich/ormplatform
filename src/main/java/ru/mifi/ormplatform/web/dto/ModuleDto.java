package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * DTO для модуля курса.
 * Включает базовые поля модуля и список уроков.
 */
public class ModuleDto {

    private Long id;
    private String title;
    private Integer orderIndex;
    private String description;
    private List<LessonDto> lessons;

    public ModuleDto() {
        // пустой конструктор для сериализации
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

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }
}

