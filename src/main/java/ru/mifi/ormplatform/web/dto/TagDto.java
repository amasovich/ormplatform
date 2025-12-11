package ru.mifi.ormplatform.web.dto;

/**
 * DTO тега курсов.
 * Используется для создания, обновления и отображения тегов.
 */
public class TagDto {

    private Long id;
    private String name;

    public TagDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
