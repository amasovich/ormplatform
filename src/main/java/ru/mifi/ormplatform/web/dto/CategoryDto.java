package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO категории курса.
 *
 * Используется в:
 *  - GET /api/categories
 *  - POST /api/categories
 *  - PUT /api/categories/{id}
 *
 * Минимальная сущность: id + name.
 */
public class CategoryDto {

    /** Идентификатор категории */
    private Long id;

    /**
     * Название категории.
     * Обязательное поле при создании / обновлении.
     */
    @NotBlank(message = "Название категории обязательно")
    private String name;

    /**
     * Пустой конструктор — требуется для сериализации Jackson.
     */
    public CategoryDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}

