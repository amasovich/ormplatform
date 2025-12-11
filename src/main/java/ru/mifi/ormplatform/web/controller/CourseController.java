package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.service.CourseService;
import ru.mifi.ormplatform.service.ModuleService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.CourseMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с курсами.
 * Реализованы операции:
 * - получение всех курсов;
 * - получение подробностей курса;
 * - поиск по названию;
 * - фильтрация по категории и преподавателю;
 * - создание, обновление и удаление курса;
 * - получение модулей курса;
 * - добавление тега к курсу.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;
    private final ModuleService moduleService;

    public CourseController(CourseService courseService,
                            CourseMapper courseMapper,
                            ModuleService moduleService) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
        this.moduleService = moduleService;
    }

    /**
     * Получаю список всех курсов.
     *
     * GET /api/courses
     */
    @GetMapping
    public ResponseEntity<List<CourseSummaryDto>> getAllCourses() {
        List<CourseSummaryDto> result = courseService.findAllCourses()
                .stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю подробную структуру курса.
     *
     * GET /api/courses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> getCourseDetails(@PathVariable Long id) {
        Course course = courseService.getByIdOrThrow(id);
        return ResponseEntity.ok(courseMapper.toDetailsDto(course));
    }

    /**
     * Поиск курсов по части названия.
     *
     * GET /api/courses/search?title=java
     */
    @GetMapping("/search")
    public ResponseEntity<List<CourseSummaryDto>> searchCourses(
            @RequestParam("title") String title) {

        List<CourseSummaryDto> result = courseService.searchByTitle(title)
                .stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю курсы категории.
     *
     * GET /api/courses/by-category/{categoryId}
     */
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<CourseSummaryDto>> getCoursesByCategory(
            @PathVariable Long categoryId) {

        List<CourseSummaryDto> result = courseService.findByCategory(categoryId)
                .stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю курсы преподавателя.
     *
     * GET /api/courses/by-teacher/{teacherId}
     */
    @GetMapping("/by-teacher/{teacherId}")
    public ResponseEntity<List<CourseSummaryDto>> getCoursesByTeacher(
            @PathVariable Long teacherId) {

        List<CourseSummaryDto> result = courseService.findByTeacher(teacherId)
                .stream()
                .map(courseMapper::toSummaryDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Создаю новый курс.
     *
     * POST /api/courses
     *
     * @param request DTO с параметрами курса.
     * @return созданный курс.
     */
    @PostMapping
    public ResponseEntity<CourseDetailsDto> createCourse(
            @Valid @RequestBody CreateCourseRequestDto request) {

        Course course = courseService.createCourse(
                request.getTitle(),
                request.getDescription(),
                request.getCategoryId(),
                request.getTeacherId(),
                request.getDuration(),
                request.getStartDate()
        );

        CourseDetailsDto dto = courseMapper.toDetailsDto(course);

        return ResponseEntity.created(URI.create("/api/courses/" + course.getId()))
                .body(dto);
    }

    /**
     * Обновляю существующий курс.
     *
     * PUT /api/courses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseDetailsDto> updateCourse(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCourseRequestDto request) {

        Course course = courseService.getByIdOrThrow(id);

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setDuration(request.getDuration());
        course.setStartDate(request.getStartDate());

        Course saved = courseService.save(course);

        return ResponseEntity.ok(courseMapper.toDetailsDto(saved));
    }

    /**
     * Удаляю курс.
     *
     * DELETE /api/courses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получаю модули курса.
     *
     * GET /api/courses/{id}/modules
     */
    @GetMapping("/{id}/modules")
    public ResponseEntity<List<ModuleDto>> getCourseModules(@PathVariable Long id) {
        List<Module> modules = moduleService.findByCourse(id);

        List<ModuleDto> result = modules.stream()
                .map(courseMapper::toModuleDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Добавляю тег к курсу.
     *
     * POST /api/courses/{id}/tags/{tagId}
     */
    @PostMapping("/{id}/tags/{tagId}")
    public ResponseEntity<CourseDetailsDto> addTag(
            @PathVariable Long id,
            @PathVariable Long tagId) {

        Course course = courseService.addTagToCourse(id, tagId);

        return ResponseEntity.ok(courseMapper.toDetailsDto(course));
    }
}
