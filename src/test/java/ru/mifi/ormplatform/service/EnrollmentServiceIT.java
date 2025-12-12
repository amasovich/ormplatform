package ru.mifi.ormplatform.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class EnrollmentServiceIT {

    @Autowired private EnrollmentService enrollmentService;

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;

    private User student;
    private User teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        // ---------- Users ----------
        teacher = new User();
        teacher.setName("Teacher");
        teacher.setEmail("teacher@edu.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        student = new User();
        student.setName("Student");
        student.setEmail("student@edu.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // ---------- Category ----------
        Category category = new Category();
        category.setName("Education");
        category = categoryRepository.save(category);

        // ---------- Course ----------
        course = new Course();
        course.setTitle("Enrollment Course");
        course.setDescription("Test enrollments");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(5);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);
    }

    // ---------------------------------------------------------------------
    // ENROLL
    // ---------------------------------------------------------------------

    @Test
    void student_can_enroll_to_course() {
        Enrollment enrollment =
                enrollmentService.enrollStudent(course.getId(), student.getId());

        assertThat(enrollment).isNotNull();
        assertThat(enrollment.getStudent().getId()).isEqualTo(student.getId());
        assertThat(enrollment.getCourse().getId()).isEqualTo(course.getId());
        assertThat(enrollment.getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
    }

    @Test
    void student_cannot_enroll_twice_active() {
        enrollmentService.enrollStudent(course.getId(), student.getId());

        assertThrows(ValidationException.class, () ->
                enrollmentService.enrollStudent(course.getId(), student.getId())
        );
    }

    @Test
    void only_student_can_enroll() {
        assertThrows(ValidationException.class, () ->
                enrollmentService.enrollStudent(course.getId(), teacher.getId())
        );
    }

    // ---------------------------------------------------------------------
    // UPDATE STATUS
    // ---------------------------------------------------------------------

    @Test
    void can_update_enrollment_status() {
        Enrollment enrollment =
                enrollmentService.enrollStudent(course.getId(), student.getId());

        Enrollment updated =
                enrollmentService.updateStatus(enrollment.getId(), EnrollmentStatus.CANCELLED);

        assertThat(updated.getStatus()).isEqualTo(EnrollmentStatus.CANCELLED);
    }

    // ---------------------------------------------------------------------
    // FIND
    // ---------------------------------------------------------------------

    @Test
    void can_find_enrollments_by_student() {
        enrollmentService.enrollStudent(course.getId(), student.getId());

        List<Enrollment> enrollments =
                enrollmentService.findByStudent(student.getId());

        assertThat(enrollments).hasSize(1);
    }

    @Test
    void can_find_enrollments_by_course() {
        enrollmentService.enrollStudent(course.getId(), student.getId());

        List<Enrollment> enrollments =
                enrollmentService.findByCourse(course.getId());

        assertThat(enrollments).hasSize(1);
    }

    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------

    @Test
    void can_delete_enrollment() {
        Enrollment enrollment =
                enrollmentService.enrollStudent(course.getId(), student.getId());

        enrollmentService.delete(enrollment.getId());

        assertThat(enrollmentService.findById(enrollment.getId())).isEmpty();
    }
}
