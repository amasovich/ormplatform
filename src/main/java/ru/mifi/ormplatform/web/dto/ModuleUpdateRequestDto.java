package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Positive;

/**
 * DTO для частичного обновления модуля курса.
 *
 * Используется в:
 *   PUT /api/modules/{id}
 *
 * Все поля являются необязательными (partial update):
 * - null значит «оставить как есть».
 */
public class ModuleUpdateRequestDto {

    /** Новое название модуля (опционально) */
    private String title;

    /**
     * Новый порядковый номер модуля.
     * Может быть null — тогда порядок не меняется.
     */
    @Positive(message = "Порядковый номер должен быть положительным числом")
    private Integer orderIndex;

    /** Новое описание модуля (опционально) */
    private String description;

    /**
     * Пустой конструктор для Jackson.
     */
    public ModuleUpdateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public String getTitle() {
        return title;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public String getDescription() {
        return description;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
