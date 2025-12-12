package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.service.SubmissionService;
import ru.mifi.ormplatform.web.dto.SubmissionDto;
import ru.mifi.ormplatform.web.dto.SubmissionGradeDto;
import ru.mifi.ormplatform.web.dto.SubmissionRequestDto;
import ru.mifi.ormplatform.web.mapper.SubmissionMapper;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с отправленными решениями (Submission).
 *
 * Поддерживаемые сценарии:
 * - студент сдаёт задание;
 * - преподаватель оценивает решение;
 * - получение всех решений по заданию;
 * - получение всех решений студента.
 */
@RestController
@RequestMapping("/api")
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    public SubmissionController(SubmissionService submissionService,
                                SubmissionMapper submissionMapper) {
        this.submissionService = submissionService;
        this.submissionMapper = submissionMapper;
    }

    /**
     * Студент отправляет решение на задание.
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
