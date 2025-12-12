package ru.mifi.ormplatform.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SubmissionServiceIT {

    @Autowired private SubmissionService submissionService;

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private LessonRepository lessonRepository;
    @Autowired private AssignmentRepository assignmentRepository;

    private User student;
    private User teacher;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        // ---------- Users ----------
        teacher = new User();
        teacher.setName("Teacher");
        teacher.setEmail("teacher@it.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        student = new User();
        student.setName("Student");
        student.setEmail("student@it.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        // ---------- Category ----------
        Category category = new Category();
        category.setName("Testing");
        category = categoryRepository.save(category);

        // ---------- Course ----------
        Course course = new Course();
        course.setTitle("Submission Course");
        course.setDescription("Course with assignments");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(4);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // ---------- Module ----------
        Module module = new Module();
        module.setCourse(course);
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module = moduleRepository.save(module);

        // ---------- Lesson ----------
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        lesson.setTitle("Lesson 1");
        lesson.setContent("Content");
        lesson = lessonRepository.save(lesson);

        // ---------- Assignment ----------
        assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle("Assignment 1");
        assignment.setDescription("Do something");
        assignment.setMaxScore(10);
        assignment = assignmentRepository.save(assignment);
    }

    // ---------------------------------------------------------------------
    // SUBMIT
    // ---------------------------------------------------------------------

    @Test
    void student_can_submit_assignment() {
        Submission submission = submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "My solution",
                LocalDateTime.now()
        );

        assertThat(submission).isNotNull();
        assertThat(submission.getStudent().getId()).isEqualTo(student.getId());
        assertThat(submission.getAssignment().getId()).isEqualTo(assignment.getId());
        assertThat(submission.getScore()).isNull();
    }

    @Test
    void student_cannot_submit_same_assignment_twice() {
        submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "First try",
                LocalDateTime.now()
        );

        assertThrows(ValidationException.class, () ->
                submissionService.submitAssignment(
                        assignment.getId(),
                        student.getId(),
                        "Second try",
                        LocalDateTime.now()
                )
        );
    }

    @Test
    void only_student_can_submit_assignment() {
        assertThrows(ValidationException.class, () ->
                submissionService.submitAssignment(
                        assignment.getId(),
                        teacher.getId(),
                        "Teacher solution",
                        LocalDateTime.now()
                )
        );
    }

    // ---------------------------------------------------------------------
    // GRADING
    // ---------------------------------------------------------------------

    @Test
    void teacher_can_grade_submission() {
        Submission submission = submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "Solution",
                LocalDateTime.now()
        );

        Submission graded = submissionService.gradeSubmission(
                submission.getId(),
                8,
                "Good job"
        );

        assertThat(graded.getScore()).isEqualTo(8);
        assertThat(graded.getFeedback()).isEqualTo("Good job");
    }

    // ---------------------------------------------------------------------
    // FIND
    // ---------------------------------------------------------------------

    @Test
    void can_find_submissions_by_assignment() {
        submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "Solution",
                LocalDateTime.now()
        );

        List<Submission> submissions =
                submissionService.findByAssignment(assignment.getId());

        assertThat(submissions).hasSize(1);
    }

    @Test
    void can_find_submissions_by_student() {
        submissionService.submitAssignment(
                assignment.getId(),
                student.getId(),
                "Solution",
                LocalDateTime.now()
        );

        List<Submission> submissions =
                submissionService.findByStudent(student.getId());

        assertThat(submissions).hasSize(1);
    }
}
