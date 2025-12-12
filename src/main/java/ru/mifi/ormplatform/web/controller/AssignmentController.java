package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.service.AssignmentService;
import ru.mifi.ormplatform.web.dto.AssignmentDto;
import ru.mifi.ormplatform.web.mapper.AssignmentMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с заданиями (Assignment).
 *
 * Отвечает ТОЛЬКО за задания:
 * - получение заданий урока.
 *
 * Работа с отправленными решениями (Submission)
 * вынесена в SubmissionController.
 */
@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final AssignmentMapper assignmentMapper;

    public AssignmentController(AssignmentService assignmentService,
                                AssignmentMapper assignmentMapper) {
        this.assignmentService = assignmentService;
        this.assignmentMapper = assignmentMapper;
    }

    /**
     * Получить список всех заданий урока.
     *
     * GET /api/lessons/{lessonId}/assignments
     */
    @GetMapping("/lessons/{lessonId}/assignments")
    public ResponseEntity<List<AssignmentDto>> getAssignmentsByLesson(
            @PathVariable Long lessonId) {

        List<AssignmentDto> result = assignmentService.findByLesson(lessonId)
                .stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
