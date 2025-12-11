package ru.mifi.ormplatform.web.dto;

/**
 * DTO для создания урока.
 */
public class LessonCreateRequestDto {

    private String title;
    private String content;
    private String videoUrl;

    public LessonCreateRequestDto() {}

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
