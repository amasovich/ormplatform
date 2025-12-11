package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.EnrollmentRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.EnrollmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация EnrollmentService.
 * Включает строгую бизнес-валидацию, корректную работу со статусами,
 * проверки ролей и стандартные исключения.
 */
@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository,
                                 CourseRepository courseRepository,
                                 UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // ============================================================================
    //                              ENROLL STUDENT
    // ============================================================================

    @Override
    public Enrollment enrollStudent(Long courseId, Long studentId) {

        // ---- Валидация параметров ----
        if (courseId == null) {
            throw new ValidationException("courseId is required");
        }
        if (studentId == null) {
            throw new ValidationException("studentId is required");
        }

        // ---- Загрузка курса ----
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + courseId));

        // ---- Загрузка студента ----
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + studentId));

        // ---- Проверка роли ----
        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException("Only STUDENT can be enrolled in a course");
        }

        // ---- Проверка существующей записи ----
        Optional<Enrollment> existing =
                enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId);

        if (existing.isPresent() && existing.get().getStatus() == EnrollmentStatus.ACTIVE) {
            throw new ValidationException("Student is already enrolled in this course");
        }

        // ---- Создание новой записи ----
        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }

    // ============================================================================
    //                                    READ
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findAllByStudent_Id(studentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByCourse(Long courseId) {
        return enrollmentRepository.findAllByCourse_Id(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enrollment> findById(Long id) {
        return enrollmentRepository.findById(id);
    }

    // ============================================================================
    //                                    UPDATE
    // ============================================================================

    @Override
    public Enrollment updateStatus(Long id, EnrollmentStatus status) {

        if (status == null) {
            throw new ValidationException("Enrollment status cannot be null");
        }

        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Enrollment not found: id=" + id));

        enrollment.setStatus(status);
        return enrollmentRepository.save(enrollment);
    }

    // ============================================================================
    //                                    DELETE
    // ============================================================================

    @Override
    public void delete(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Enrollment not found: id=" + id));

        enrollmentRepository.delete(enrollment);
    }
}
