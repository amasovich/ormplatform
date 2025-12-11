package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.web.dto.LessonDto;
import ru.mifi.ormplatform.web.dto.ModuleDto;

import java.util.stream.Collectors;

/**
 * Маппер для преобразования сущностей Module ↔ DTO.
 */
@Component
public class ModuleMapper {

    /**
     * Преобразование сущности Module в DTO.
     */
    public ModuleDto toDto(Module module) {
        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrderIndex(module.getOrderIndex());

        if (module.getLessons() != null) {
            dto.setLessons(
                    module.getLessons().stream()
                            .map(this::toLessonDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    /**
     * Преобразование Lesson → LessonDto.
     * (Мини-метод, нужен и для CourseMapper)
     */
    private LessonDto toLessonDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}
