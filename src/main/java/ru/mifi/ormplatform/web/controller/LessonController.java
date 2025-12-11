package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.service.LessonService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.LessonMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с уроками курса.
 */
@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    public LessonController(LessonService lessonService,
                            LessonMapper lessonMapper) {
        this.lessonService = lessonService;
        this.lessonMapper = lessonMapper;
    }

    /**
     * Создать урок в модуле.
     * POST /api/lessons/module/{moduleId}
     */
    @PostMapping("/module/{moduleId}")
    public ResponseEntity<LessonDto> createLesson(
            @PathVariable Long moduleId,
            @Valid @RequestBody LessonCreateRequestDto request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Lesson title cannot be empty");
        }

        Lesson lesson = lessonService.createLesson(
                moduleId,
                request.getTitle(),
                request.getContent(),
                request.getVideoUrl()
        );

        return ResponseEntity
                .created(URI.create("/api/lessons/" + lesson.getId()))
                .body(lessonMapper.toDto(lesson));
    }

    /**
     * Обновить урок.
     * PUT /api/lessons/{lessonId}
     */
    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonDto> updateLesson(
            @PathVariable Long lessonId,
            @Valid @RequestBody LessonUpdateRequestDto request) {

        Lesson lesson = lessonService.updateLesson(
                lessonId,
                request.getTitle(),
                request.getContent(),
                request.getVideoUrl()
        );

        return ResponseEntity.ok(lessonMapper.toDto(lesson));
    }

    /**
     * Удалить урок.
     * DELETE /api/lessons/{lessonId}
     */
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить урок по id.
     * GET /api/lessons/{lessonId}
     */
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long lessonId) {

        Lesson lesson = lessonService.findById(lessonId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson not found: id=" + lessonId));

        return ResponseEntity.ok(lessonMapper.toDto(lesson));
    }

    /**
     * Получить все уроки модуля.
     * GET /api/lessons/module/{moduleId}
     */
    @GetMapping("/module/{moduleId}")
    public ResponseEntity<List<LessonDto>> getLessonsByModule(@PathVariable Long moduleId) {

        List<LessonDto> lessons = lessonService.findByModule(moduleId)
                .stream()
                .map(lessonMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(lessons);
    }
}
