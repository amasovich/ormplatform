package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.web.dto.CourseDetailsDto;
import ru.mifi.ormplatform.web.dto.CourseSummaryDto;
import ru.mifi.ormplatform.web.dto.LessonDto;
import ru.mifi.ormplatform.web.dto.ModuleDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Маппер между JPA-сущностями курса и DTO.
 * Здесь я явно контролирую, какие данные уходят наружу в REST-слой.
 */
@Component
public class CourseMapper {

    /**
     * Преобразую сущность курса в краткий DTO для списков.
     *
     * @param course исходная JPA-сущность.
     * @return DTO для списка курсов.
     */
    public CourseSummaryDto toSummaryDto(Course course) {
        CourseSummaryDto dto = new CourseSummaryDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setStartDate(course.getStartDate());

        if (course.getCategory() != null) {
            dto.setCategoryName(course.getCategory().getName());
        }

        if (course.getTeacher() != null) {
            dto.setTeacherName(course.getTeacher().getName());
        }

        List<String> tagNames = course.getTags()
                .stream()
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .collect(Collectors.toList());
        dto.setTags(tagNames);

        return dto;
    }

    /**
     * Преобразую сущность курса в подробный DTO со структурой модулей и уроков.
     *
     * @param course исходная JPA-сущность.
     * @return DTO для подробного просмотра курса.
     */
    public CourseDetailsDto toDetailsDto(Course course) {
        CourseDetailsDto dto = new CourseDetailsDto();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setDuration(course.getDuration());
        dto.setStartDate(course.getStartDate());

        if (course.getCategory() != null) {
            dto.setCategoryName(course.getCategory().getName());
        }

        if (course.getTeacher() != null) {
            dto.setTeacherName(course.getTeacher().getName());
        }

        List<String> tagNames = course.getTags()
                .stream()
                .filter(Objects::nonNull)
                .map(Tag::getName)
                .collect(Collectors.toList());
        dto.setTags(tagNames);

        List<ModuleDto> moduleDtos = course.getModules()
                .stream()
                .map(this::toModuleDto)
                .sorted((a, b) -> Integer.compare(
                        a.getOrderIndex() != null ? a.getOrderIndex() : 0,
                        b.getOrderIndex() != null ? b.getOrderIndex() : 0))
                .collect(Collectors.toList());
        dto.setModules(moduleDtos);

        return dto;
    }

    private ModuleDto toModuleDto(Module module) {
        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setOrderIndex(module.getOrderIndex());
        dto.setDescription(module.getDescription());

        List<LessonDto> lessonDtos = module.getLessons()
                .stream()
                .map(this::toLessonDto)
                .collect(Collectors.toList());
        dto.setLessons(lessonDtos);

        return dto;
    }

    private LessonDto toLessonDto(Lesson lesson) {
        LessonDto dto = new LessonDto();
        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());
        return dto;
    }
}

