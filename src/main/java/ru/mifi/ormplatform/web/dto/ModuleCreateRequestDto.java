package ru.mifi.ormplatform.web.dto;

/**
 * DTO для создания модуля курса.
 * Используется в POST /api/courses/{courseId}/modules
 */
public class ModuleCreateRequestDto {

    private String title;
    private Integer orderIndex;
    private String description;

    public ModuleCreateRequestDto() {
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
}
