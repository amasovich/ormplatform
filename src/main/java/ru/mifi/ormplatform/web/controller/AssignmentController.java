package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.service.AssignmentService;
import ru.mifi.ormplatform.service.SubmissionService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.AssignmentMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с заданиями и отправленными решениями.
 *
 * Поддерживаются сценарии:
 * - получение заданий урока;
 * - отправка решения студентом;
 * - получение решений по заданию;
 * - получение решений конкретного студента;
 * - оценивание решения преподавателем.
 */
@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;
    private final AssignmentMapper assignmentMapper;

    public AssignmentController(AssignmentService assignmentService,
                                SubmissionService submissionService,
                                AssignmentMapper assignmentMapper) {
        this.assignmentService = assignmentService;
        this.submissionService = submissionService;
        this.assignmentMapper = assignmentMapper;
    }

    // =============================
    //    ЗАДАНИЯ УРОКА
    // =============================

    /**
     * Возвращаю список заданий для указанного урока.
     *
     * Пример:
     * GET /api/lessons/2/assignments
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

    // =============================
    //    СОЗДАНИЕ РЕШЕНИЯ
    // =============================

    /**
     * Студент сдаёт задание.
     *
     * POST /api/assignments/{assignmentId}/submissions
     */
    @PostMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<SubmissionDto> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody SubmissionRequestDto request) {

        Submission submission = submissionService.submitAssignment(
                assignmentId,
                request.getStudentId(),
                request.getContent(),
                LocalDateTime.now()
        );

        return ResponseEntity.created(
                URI.create("/api/submissions/" + submission.getId())
        ).body(assignmentMapper.toDto(submission));
    }

    // =============================
    //    ПОЛУЧЕНИЕ РЕШЕНИЙ УРОКА
    // =============================

    /**
     * Получаю все решения по заданию.
     *
     * GET /api/assignments/{assignmentId}/submissions
     */
    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {

        List<SubmissionDto> result = submissionService.findByAssignment(assignmentId)
                .stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю все решения, которые сдал студент.
     *
     * GET /api/students/{studentId}/submissions
     */
    @GetMapping("/students/{studentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByStudent(
            @PathVariable Long studentId) {

        List<SubmissionDto> result = submissionService.findByStudent(studentId)
                .stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // =============================
    //    ОЦЕНИВАНИЕ ПРЕПОДАВАТЕЛЕМ
    // =============================

    /**
     * Преподаватель выставляет оценку и комментарий существующей отправке.
     *
     * PUT /api/submissions/{submissionId}/grade
     */
    @PutMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<SubmissionDto> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestBody SubmissionGradeDto request) {

        Submission updated = submissionService.gradeSubmission(
                submissionId,
                request.getScore(),
                request.getFeedback()
        );

        return ResponseEntity.ok(assignmentMapper.toDto(updated));
    }
}
