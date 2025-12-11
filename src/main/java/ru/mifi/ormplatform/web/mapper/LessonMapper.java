package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.web.dto.LessonDto;

/**
 * Маппер между сущностью {@link Lesson} и DTO {@link LessonDto}.
 * Отвечает за формирование данных урока для REST-слоя.
 *
 * <p>Методы безопасны к null — передача null возвращает null.</p>
 */
@Component
public class LessonMapper {

    /**
     * Преобразует сущность {@link Lesson} в {@link LessonDto}.
     *
     * @param lesson исходная сущность. Может быть null.
     * @return DTO или null, если lesson == null.
     */
    public LessonDto toDto(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

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
