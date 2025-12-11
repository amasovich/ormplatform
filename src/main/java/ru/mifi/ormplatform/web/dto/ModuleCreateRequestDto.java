package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO для создания модуля курса.
 * Используется в POST /api/courses/{courseId}/modules
 */
public class ModuleCreateRequestDto {

    @NotBlank(message = "Название модуля не может быть пустым")
    @Size(max = 255, message = "Название модуля должно быть не длиннее 255 символов")
    private String title;

    @NotNull(message = "Порядок модуля (orderIndex) обязателен")
    @Min(value = 1, message = "Порядковый номер модуля должен быть >= 1")
    private Integer orderIndex;

    @Size(max = 2000, message = "Описание модуля не может превышать 2000 символов")
    private String description;

    public ModuleCreateRequestDto() {
        // Пустой конструктор необходим для Jackson
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
