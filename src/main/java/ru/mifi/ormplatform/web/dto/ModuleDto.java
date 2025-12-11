package ru.mifi.ormplatform.web.dto;

import java.util.List;

/**
 * DTO модуля курса.
 * Используется в деталях курса и в ModuleController.
 */
public class ModuleDto {

    private Long id;
    private String title;
    private String description;
    private Integer orderIndex;
    private List<LessonDto> lessons;

    public ModuleDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public List<LessonDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonDto> lessons) {
        this.lessons = lessons;
    }
}
