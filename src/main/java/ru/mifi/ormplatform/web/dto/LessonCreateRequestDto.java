package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO для создания урока.
 * Используется в POST /api/modules/{moduleId}/lessons.
 */
public class LessonCreateRequestDto {

    /** Название урока — обязательное поле. */
    @NotBlank(message = "Название урока обязательно")
    private String title;

    /** Текстовое содержимое урока. Может быть пустым. */
    private String content;

    /** Ссылка на видеоурок (опционально). */
    private String videoUrl;

    public LessonCreateRequestDto() {
        // пустой конструктор для Jackson
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
