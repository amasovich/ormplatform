package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для вывода урока наружу.
 * Используется как часть ModuleDto и в LessonController.
 */
public class LessonDto {

    /** Идентификатор урока. */
    private Long id;

    /** Идентификатор модуля, к которому относится урок. */
    private Long moduleId;

    /** Название урока (обязательно). */
    @NotBlank(message = "Название урока не может быть пустым")
    private String title;

    /** Содержимое урока (конспект, текстовое описание). Может быть пустым. */
    private String content;

    /** Ссылка на видеоматериал. Может быть null. */
    private String videoUrl;

    public LessonDto() {
        // пустой конструктор для Jackson
    }

    // ======== GETTERS ========

    public Long getId() {
        return id;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    // ======== SETTERS ========

    public void setId(Long id) {
        this.id = id;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
