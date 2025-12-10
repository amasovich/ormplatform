package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.service.EnrollmentService;
import ru.mifi.ormplatform.web.dto.EnrollmentDto;
import ru.mifi.ormplatform.web.dto.EnrollmentRequestDto;
import ru.mifi.ormplatform.web.mapper.EnrollmentMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с записями студентов на курсы.
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
     * Записать студента на курс.
     *
     * Пример запроса:
     * POST /api/courses/1/enrollments
     * {
     *   "studentId": 2
     * }
     */
    @PostMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<EnrollmentDto> enrollStudent(
            @PathVariable Long courseId,
            @RequestBody EnrollmentRequestDto request) {

        Enrollment enrollment = enrollmentService.enrollStudent(courseId, request.getStudentId());
        return ResponseEntity.ok(enrollmentMapper.toDto(enrollment));
    }

    /**
     * Получить все курсы, на которые записан студент.
     *
     * GET /api/students/2/enrollments
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
     * Получить всех студентов, записанных на указанный курс.
     *
     * GET /api/courses/1/enrollments
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
}
