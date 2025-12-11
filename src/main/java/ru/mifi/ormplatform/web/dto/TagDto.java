package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO тега курсов.
 * Используется для:
 *  - отображения тега,
 *  - создания нового тега,
 *  - обновления существующего тега.
 */
public class TagDto {

    private Long id;

    /**
     * Название тега.
     * Должно быть непустым при создании или обновлении.
     */
    @NotBlank(message = "Название тега не может быть пустым")
    private String name;

    public TagDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ========= GETTERS =========

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // ========= SETTERS =========

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
