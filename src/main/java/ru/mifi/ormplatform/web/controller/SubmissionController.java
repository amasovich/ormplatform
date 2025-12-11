package ru.mifi.ormplatform.web.controller;

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
 * REST-контроллер для работы с отправленными решениями (Submission):
 * — студент сдаёт задание,
 * — преподаватель ставит оценку,
 * — получение всех решений по заданию,
 * — получение всех решений конкретного студента.
 *
 * AssignmentController отвечает за задания,
 * а SubmissionController — за сами отправленные решения.
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

    // ========================================================================
    // 1. СТУДЕНТ СДАЁТ РЕШЕНИЕ
    // ========================================================================

    /**
     * Студент отправляет решение на задание.
     *
     * POST /api/assignments/{assignmentId}/submissions
     *
     * @param assignmentId ID задания
     * @param request тело запроса: studentId + content
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
        ).body(submissionMapper.toDto(submission));
    }

    // ========================================================================
    // 2. ВСЕ РЕШЕНИЯ ПО ЗАДАНИЮ
    // ========================================================================

    /**
     * Получаю список всех решений, отправленных на конкретное задание.
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

    // ========================================================================
    // 3. ВСЕ РЕШЕНИЯ КОНКРЕТНОГО СТУДЕНТА
    // ========================================================================

    /**
     * Получаю список решений, которые сдал конкретный студент.
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

    // ========================================================================
    // 4. ПРЕПОДАВАТЕЛЬ СТАВИТ ОЦЕНКУ
    // ========================================================================

    /**
     * Преподаватель оценивает решение и добавляет комментарий.
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

        return ResponseEntity.ok(submissionMapper.toDto(updated));
    }
}
