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

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Маппер между сущностями курса и DTO.
 * Управляет тем, какие данные видит REST-клиент.
 */
@Component
public class CourseMapper {

    /**
     * Преобразование сущности Course → краткое DTO.
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

        dto.setTags(
                course.getTags() == null ? Collections.emptyList() :
                        course.getTags().stream()
                                .filter(Objects::nonNull)
                                .map(Tag::getName)
                                .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Преобразование сущности Course → детальное DTO со структурой модулей и уроков.
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

        dto.setTags(
                course.getTags() == null ? Collections.emptyList() :
                        course.getTags().stream()
                                .filter(Objects::nonNull)
                                .map(Tag::getName)
                                .collect(Collectors.toList())
        );

        dto.setModules(
                course.getModules() == null ? Collections.emptyList() :
                        course.getModules().stream()
                                .map(this::toModuleDto)
                                .sorted((a, b) -> Integer.compare(
                                        a.getOrderIndex() == null ? 0 : a.getOrderIndex(),
                                        b.getOrderIndex() == null ? 0 : b.getOrderIndex()))
                                .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Преобразование Module → ModuleDto
     */
    public ModuleDto toModuleDto(Module module) {
        ModuleDto dto = new ModuleDto();
        dto.setId(module.getId());
        dto.setTitle(module.getTitle());
        dto.setDescription(module.getDescription());
        dto.setOrderIndex(module.getOrderIndex());

        dto.setLessons(
                module.getLessons() == null ? Collections.emptyList() :
                        module.getLessons().stream()
                                .map(this::toLessonDto)
                                .collect(Collectors.toList())
        );

        return dto;
    }

    /**
     * Преобразование Lesson → LessonDto
     */
    public LessonDto toLessonDto(Lesson lesson) {
        LessonDto dto = new LessonDto();

        dto.setId(lesson.getId());
        dto.setTitle(lesson.getTitle());
        dto.setContent(lesson.getContent());
        dto.setVideoUrl(lesson.getVideoUrl());

        return dto;
    }
}
