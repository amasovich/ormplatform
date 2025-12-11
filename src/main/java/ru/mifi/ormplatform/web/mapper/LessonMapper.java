package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.web.dto.LessonDto;

/**
 * Маппер уроков в DTO.
 */
@Component
public class LessonMapper {

    /**
     * Преобразование Lesson → LessonDto.
     */
    public LessonDto toDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());

        if (lesson.getModule() != null) {
            dto.setModuleId(lesson.getModule().getId());
        }

        return dto;
    }
}
