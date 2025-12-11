package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.service.LessonService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.LessonMapper;

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
     */
    @PostMapping("/module/{moduleId}")
    public ResponseEntity<LessonDto> createLesson(
            @PathVariable Long moduleId,
            @RequestBody LessonCreateRequestDto request) {

        Lesson lesson = lessonService.createLesson(
                moduleId,
                request.getTitle(),
                request.getContent(),
                request.getVideoUrl()
        );

        return ResponseEntity.ok(lessonMapper.toDto(lesson));
    }

    /**
     * Обновить урок.
     */
    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonDto> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody LessonUpdateRequestDto request) {

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
     */
    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить урок по id.
     */
    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonDto> getLesson(@PathVariable Long lessonId) {
        Lesson lesson = lessonService.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Урок не найден"));
        return ResponseEntity.ok(lessonMapper.toDto(lesson));
    }

    /**
     * Получить все уроки модуля.
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
