package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для частичного обновления урока.
 * Все поля опциональны. Если поле = null — оно не изменяется.
 */
public class LessonUpdateRequestDto {

    /**
     * Новое название урока.
     * Может быть null (тогда не обновляем).
     */
    @NotBlank(message = "Название урока не может быть пустым, если указано")
    private String title;

    /**
     * Новое текстовое содержимое урока.
     * Может быть null.
     */
    private String content;

    /**
     * Новая ссылка на видео.
     * Может быть null.
     */
    private String videoUrl;

    public LessonUpdateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======== GETTERS ========

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
