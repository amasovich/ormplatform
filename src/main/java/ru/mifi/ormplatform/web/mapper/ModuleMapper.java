package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.web.dto.LessonDto;
import ru.mifi.ormplatform.web.dto.ModuleDto;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Маппер между сущностью {@link Module} и DTO {@link ModuleDto}.
 * Отвечает за преобразование модулей курса и вложенных уроков.
 *
 * <p>Все методы null-safe, коллекции возвращаются пустыми, а не null.</p>
 */
@Component
public class ModuleMapper {

    /**
     * Преобразует сущность {@link Module} в {@link ModuleDto}.
     * Если передан null — возвращает null.
     *
     * @param module исходная JPA-сущность.
     * @return DTO или null.
     */
    public ModuleDto toDto(Module module) {
        if (module == null) {
            return null;
        }

        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrderIndex(module.getOrderIndex());

        // Уроки (null-safe)
        if (module.getLessons() != null) {
            dto.setLessons(
                    module.getLessons().stream()
                            .map(this::toLessonDto)
                            .collect(Collectors.toList())
            );
        } else {
            dto.setLessons(Collections.emptyList());
        }

        return dto;
    }

    /**
     * Преобразование {@link Lesson} → {@link LessonDto}.
     * Метод используется внутри маппера модулей и другими компонентами.
     *
     * @param lesson сущность урока.
     * @return DTO урока или null.
     */
    private LessonDto toLessonDto(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}
