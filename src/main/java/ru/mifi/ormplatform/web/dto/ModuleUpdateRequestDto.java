package ru.mifi.ormplatform.web.dto;

/**
 * DTO для обновления модуля курса.
 * Используется в PUT /api/modules/{id}
 */
public class ModuleUpdateRequestDto {

    private String title;
    private Integer orderIndex;
    private String description;

    public ModuleUpdateRequestDto() {
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

