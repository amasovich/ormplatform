package ru.mifi.ormplatform.service.impl;

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
import java.util.stream.Collectors;

/**
 * Реализация сервиса управления записями на курс.
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

    @Override
    public Enrollment enrollStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пользователь с id=" + studentId + " не найден"));

        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException(
                    "Записать на курс можно только пользователя с ролью STUDENT");
        }

        // Проверяю, не записан ли студент уже на этот курс
        List<Enrollment> existing = enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getCourse().getId().equals(courseId)
                        && e.getStudent().getId().equals(studentId)
                        && e.getStatus() == EnrollmentStatus.ACTIVE)
                .collect(Collectors.toList());

        if (!existing.isEmpty()) {
            // На этом этапе просто кидаю исключение; позже можно вернуть 409 CONFLICT.
            throw new IllegalStateException(
                    "Студент уже записан на этот курс в статусе ACTIVE");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enrollment> findByCourse(Long courseId) {
        return enrollmentRepository.findAll()
                .stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .collect(Collectors.toList());
    }
}

