package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.service.EnrollmentService;
import ru.mifi.ormplatform.web.dto.EnrollmentDto;
import ru.mifi.ormplatform.web.dto.EnrollmentRequestDto;
import ru.mifi.ormplatform.web.dto.EnrollmentStatusUpdateDto;
import ru.mifi.ormplatform.web.mapper.EnrollmentMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с записями студентов на курсы.
 *
 * Поддерживаемые операции:
 * - записать студента на курс;
 * - получить записи студента;
 * - получить записи по курсу;
 * - получить запись по id;
 * - удалить запись (отписать студента);
 * - обновить статус записи.
 */
@RestController
@RequestMapping("/api")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentController(EnrollmentService enrollmentService,
                                EnrollmentMapper enrollmentMapper) {
        this.enrollmentService = enrollmentService;
        this.enrollmentMapper = enrollmentMapper;
    }

    /**
     * Записываю студента на курс.
     *
     * POST /api/courses/{courseId}/enrollments
     */
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @PathVariable Long courseId,
            @Valid @RequestBody EnrollmentRequestDto request) {

        if (request.getStudentId() == null) {
            throw new IllegalArgumentException("studentId is required");
        }

        Enrollment enrollment = enrollmentService.enrollStudent(
                courseId,
                request.getStudentId()
        );

        return ResponseEntity.created(
                URI.create("/api/enrollments/" + enrollment.getId())
        ).body(enrollmentMapper.toDto(enrollment));
    }

    /**
     * Получаю запись по идентификатору.
     *
     * GET /api/enrollments/{id}
     */
    @GetMapping("/enrollments/{id}")
    public ResponseEntity<EnrollmentDto> getEnrollmentById(@PathVariable Long id) {

        Enrollment enrollment = enrollmentService.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Enrollment not found: id=" + id));

        return ResponseEntity.ok(enrollmentMapper.toDto(enrollment));
    }

    /**
     * Курсы, на которые записан студент.
     *
     * GET /api/students/{studentId}/enrollments
     */
    @GetMapping("/students/{studentId}/enrollments")
    public ResponseEntity<List<EnrollmentDto>> getStudentEnrollments(
            @PathVariable Long studentId) {

        List<EnrollmentDto> result = enrollmentService.findByStudent(studentId)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Студенты, записанные на курс.
     *
     * GET /api/courses/{courseId}/enrollments
     */
    @GetMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<List<EnrollmentDto>> getCourseEnrollments(
            @PathVariable Long courseId) {

        List<EnrollmentDto> result = enrollmentService.findByCourse(courseId)
                .stream()
                .map(enrollmentMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Обновляю статус записи студента.
     *
     * PUT /api/enrollments/{id}/status
     */
    @PutMapping("/enrollments/{id}/status")
    public ResponseEntity<EnrollmentDto> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody EnrollmentStatusUpdateDto request) {

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }

        Enrollment enrollment = enrollmentService.updateStatus(
                id,
                request.getStatus()
        );

        return ResponseEntity.ok(enrollmentMapper.toDto(enrollment));
    }

    /**
     * Удаляю запись (отписываю студента).
     *
     * DELETE /api/enrollments/{id}
     */
    @DeleteMapping("/enrollments/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {

        enrollmentService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
