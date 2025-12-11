package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.web.dto.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Маппер для преобразования сущности {@link Course}
 * в DTO: краткое описание (CourseSummaryDto) и полное (CourseDetailsDto).
 *
 * <p>Ответственность маппера — контролировать,
 * какие данные курса и его вложенных сущностей
 * передаются REST-клиенту.</p>
 */
@Component
public class CourseMapper {

    // -------------------------------------------------------------------------
    // SUMMARY DTO
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Course} в краткое представление {@link CourseSummaryDto}.
     *
     * @param course JPA-сущность курса (не null)
     * @return DTO для списков курсов
     */
    public CourseSummaryDto toSummaryDto(Course course) {
        if (course == null) return null;

        CourseSummaryDto dto = new CourseSummaryDto();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setStartDate(course.getStartDate());

        // CATEGORY
        if (course.getCategory() != null) {
            dto.setCategoryName(course.getCategory().getName());
        }

        // TEACHER
        if (course.getTeacher() != null) {
            dto.setTeacherName(course.getTeacher().getName());
        }

        // TAGS
        dto.setTags(
                course.getTags() == null
                        ? Collections.emptyList()
                        : course.getTags().stream()
                        .filter(Objects::nonNull)
                        .map(tag -> tag.getName())
                        .collect(Collectors.toList())
        );

        return dto;
    }

    // -------------------------------------------------------------------------
    // DETAILS DTO
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Course} в детальное представление {@link CourseDetailsDto},
     * включая модули курса и вложенные уроки.
     *
     * @param course JPA-сущность курса
     * @return полная иерархическая структура курса
     */
    public CourseDetailsDto toDetailsDto(Course course) {
        if (course == null) return null;

        CourseDetailsDto dto = new CourseDetailsDto();

        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setStartDate(course.getStartDate());

        // CATEGORY
        if (course.getCategory() != null) {
            dto.setCategoryName(course.getCategory().getName());
        }

        // TEACHER
        if (course.getTeacher() != null) {
            dto.setTeacherName(course.getTeacher().getName());
        }

        // TAGS
        dto.setTags(
                course.getTags() == null
                        ? Collections.emptyList()
                        : course.getTags().stream()
                        .filter(Objects::nonNull)
                        .map(tag -> tag.getName())
                        .collect(Collectors.toList())
        );

        // MODULES
        dto.setModules(
                course.getModules() == null
                        ? Collections.emptyList()
                        : course.getModules().stream()
                        .map(this::toModuleDto)
                        .sorted(Comparator.comparing(
                                m -> m.getOrderIndex() == null ? 0 : m.getOrderIndex()
                        ))
                        .collect(Collectors.toList())
        );

        return dto;
    }

    // -------------------------------------------------------------------------
    // MODULE MAPPING
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Module} в {@link ModuleDto}.
     */
    public ModuleDto toModuleDto(Module module) {
        if (module == null) return null;

        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrderIndex(module.getOrderIndex());

        dto.setLessons(
                module.getLessons() == null
                        ? Collections.emptyList()
                        : module.getLessons().stream()
                        .map(this::toLessonDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    // -------------------------------------------------------------------------
    // LESSON MAPPING
    // -------------------------------------------------------------------------

    /**
     * Преобразует сущность {@link Lesson} в {@link LessonDto}.
     */
    public LessonDto toLessonDto(Lesson lesson) {
        if (lesson == null) return null;

        LessonDto dto = new LessonDto();

        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());

        return dto;
    }
}
