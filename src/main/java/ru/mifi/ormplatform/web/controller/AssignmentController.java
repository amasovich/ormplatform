package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.service.AssignmentService;
import ru.mifi.ormplatform.service.SubmissionService;
import ru.mifi.ormplatform.web.dto.AssignmentDto;
import ru.mifi.ormplatform.web.dto.SubmissionDto;
import ru.mifi.ormplatform.web.dto.SubmissionRequestDto;
import ru.mifi.ormplatform.web.mapper.AssignmentMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с заданиями и отправленными решениями.
 * <p>
 * Здесь я реализую учебный сценарий:
 * – получить задания по уроку;
 * – отправить решение по заданию;
 * – посмотреть все решения по заданию;
 * – посмотреть все решения конкретного студента.
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

    /**
     * Возвращаю список заданий для указанного урока.
     * <p>
     * Пример: GET /api/lessons/2/assignments
     *
     * @param lessonId идентификатор урока.
     * @return список DTO заданий.
     */
    @GetMapping("/lessons/{lessonId}/assignments")
    public ResponseEntity<List<AssignmentDto>> getAssignmentsByLesson(
            @PathVariable Long lessonId) {

        List<Assignment> assignments = assignmentService.findByLesson(lessonId);

        List<AssignmentDto> result = assignments.stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Студент сдаёт задание.
     * <p>
     * Пример запроса:
     * POST /api/assignments/1/submissions
     * {
     *   "studentId": 2,
     *   "content": "Ссылка на GitHub-репозиторий с решением"
     * }
     *
     * @param assignmentId идентификатор задания.
     * @param request      тело запроса с id студента и текстом решения.
     * @return DTO созданного Submission.
     */
    @PostMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<SubmissionDto> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestBody SubmissionRequestDto request) {

        // Здесь я использую существующий метод сервиса submitAssignment(..., submittedAt)
        Submission submission = submissionService.submitAssignment(
                assignmentId,
                request.getStudentId(),
                request.getContent(),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(assignmentMapper.toDto(submission));
    }

    /**
     * Получаю все решения по конкретному заданию (типичный сценарий для ментора).
     * <p>
     * Пример: GET /api/assignments/1/submissions
     *
     * @param assignmentId идентификатор задания.
     * @return список DTO отправленных решений.
     */
    @GetMapping("/assignments/{assignmentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByAssignment(
            @PathVariable Long assignmentId) {

        List<Submission> submissions = submissionService.findByAssignment(assignmentId);

        List<SubmissionDto> result = submissions.stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получаю все решения, которые сдал конкретный студент.
     * <p>
     * Пример: GET /api/students/2/submissions
     *
     * @param studentId идентификатор студента.
     * @return список DTO отправленных решений.
     */
    @GetMapping("/students/{studentId}/submissions")
    public ResponseEntity<List<SubmissionDto>> getSubmissionsByStudent(
            @PathVariable Long studentId) {

        List<Submission> submissions = submissionService.findByStudent(studentId);

        List<SubmissionDto> result = submissions.stream()
                .map(assignmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}

