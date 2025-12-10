package ru.mifi.ormplatform.web.dto;

/**
 * DTO для урока.
 * Здесь только то, что действительно нужно отдать наружу клиенту.
 */
public class LessonDto {

    private Long id;
    private String title;
    private String content;
    private String videoUrl;

    public LessonDto() {
        // пустой конструктор для сериализации
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}

