package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.service.CourseService;
import ru.mifi.ormplatform.web.dto.CourseDetailsDto;
import ru.mifi.ormplatform.web.dto.CourseSummaryDto;
import ru.mifi.ormplatform.web.mapper.CourseMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с курсами.
 * Здесь я пока реализую только операции чтения структуры курса.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    public CourseController(CourseService courseService,
                            CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    /**
     * Получаю список всех курсов.
     *
     * Пример запроса:
     * GET /api/courses
     *
     * @return список кратких DTO по курсам.
     */
    @GetMapping
    public ResponseEntity<List<CourseSummaryDto>> getAllCourses() {
        List<Course> courses = courseService.findAllCourses();

        List<CourseSummaryDto> result = courses.stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю подробную структуру одного курса:
     * общие поля, модули и уроки.
     *
     * Пример запроса:
     * GET /api/courses/1
     *
     * @param id идентификатор курса.
     * @return подробный DTO курса.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> getCourseDetails(@PathVariable Long id) {
        Course course = courseService.getByIdOrThrow(id);
        CourseDetailsDto dto = courseMapper.toDetailsDto(course);
        return ResponseEntity.ok(dto);
    }
}

