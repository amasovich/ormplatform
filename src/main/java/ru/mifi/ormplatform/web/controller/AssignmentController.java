package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.service.AssignmentService;
import ru.mifi.ormplatform.service.SubmissionService;
import ru.mifi.ormplatform.web.dto.*;
import ru.mifi.ormplatform.web.mapper.AssignmentMapper;
import ru.mifi.ormplatform.web.mapper.SubmissionMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с заданиями и отправленными решениями.
 *
 * Поддерживаемые сценарии:
 * - получение заданий урока;
 * - отправка решения студентом;
 * - получение всех решений задания;
 * - получение всех решений студента;
 * - оценивание преподавателем.
 */
@RestController
@RequestMapping("/api")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;
    private final AssignmentMapper assignmentMapper;
    private final SubmissionMapper submissionMapper;

    public AssignmentController(AssignmentService assignmentService,
                                SubmissionService submissionService,
                                AssignmentMapper assignmentMapper,
                                SubmissionMapper submissionMapper) {
        this.assignmentService = assignmentService;
        this.submissionService = submissionService;
        this.assignmentMapper = assignmentMapper;
        this.submissionMapper = submissionMapper;
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

    /**
     * Студент сдаёт решение задания.
     *
     * POST /api/assignments/{assignmentId}/submissions
     */
    @PostMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<SubmissionDto> submitAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody SubmissionRequestDto request) {

        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("studentId is required");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("submission content cannot be empty");
        }

        Submission submission = submissionService.submitAssignment(
                assignmentId,
                request.getStudentId(),
                request.getContent(),
                LocalDateTime.now()
        );

        return ResponseEntity.created(
                URI.create("/api/submissions/" + submission.getId())
        ).body(submissionMapper.toDto(submission));
    }

    /**
     * Получить все решения по заданию.
     *
     * GET /api/assignments/{assignmentId}/submissions
     */
    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {

        List<SubmissionDto> result = submissionService.findByAssignment(assignmentId)
                .stream()
                .map(submissionMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получить все решения студента.
     *
     * GET /api/students/{studentId}/submissions
     */
    @GetMapping("/students/{studentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByStudent(
            @PathVariable Long studentId) {

        List<SubmissionDto> result = submissionService.findByStudent(studentId)
                .stream()
                .map(submissionMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Преподаватель оценивает отправленное решение.
     *
     * PUT /api/submissions/{submissionId}/grade
     */
    @PutMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<SubmissionDto> gradeSubmission(
            @PathVariable Long submissionId,
            @Valid @RequestBody SubmissionGradeDto request) {

        if (request.getScore() == null) {
            throw new IllegalArgumentException("score is required");
        }

        Submission updated = submissionService.gradeSubmission(
                submissionId,
                request.getScore(),
                request.getFeedback()
        );

        return ResponseEntity.ok(submissionMapper.toDto(updated));
    }
}
