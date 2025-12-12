package ru.mifi.ormplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Интеграционные тесты сервиса AssignmentService.
 *
 * Проверяет:
 * - создание задания;
 * - привязку к уроку;
 * - валидацию входных данных;
 * - получение заданий по уроку.
 */
@SpringBootTest
@Transactional
class AssignmentServiceIT {

    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private LessonService lessonService;

    private Lesson lesson;

    @BeforeEach
    void setUp() {
        User teacher = userService.createUser(
                "Teacher",
                "teacher@test.com",
                UserRole.TEACHER
        );

        Category category = categoryService.createCategory("Test category");

        Course course = courseService.createCourse(
                "Test course",
                "Course description",
                category.getId(),
                teacher.getId(),
                10,
                LocalDate.now()
        );

        Module module = moduleService.createModule(
                course.getId(),
                "Module 1",
                1,
                "Module description"
        );

        lesson = lessonService.createLesson(
                module.getId(),
                "Lesson 1",
                "Lesson content",
                null
        );
    }

    @Test
    void createAssignment_success() {
        Assignment assignment = assignmentService.createAssignment(
                lesson.getId(),
                "Assignment title",
                "Assignment description",
                LocalDate.now().plusDays(7),
                10
        );

        assertThat(assignment.getId()).isNotNull();
        assertThat(assignment.getLesson().getId()).isEqualTo(lesson.getId());
        assertThat(assignment.getTitle()).isEqualTo("Assignment title");
        assertThat(assignment.getMaxScore()).isEqualTo(10);
    }

    @Test
    void createAssignment_emptyTitle_shouldFail() {
        assertThatThrownBy(() ->
                assignmentService.createAssignment(
                        lesson.getId(),
                        "",
                        "desc",
                        LocalDate.now(),
                        10
                )
        )
                .isInstanceOf(jakarta.validation.ValidationException.class)
                .hasMessageContaining("title");
    }

    @Test
    void createAssignment_emptyDescription_shouldFail() {
        assertThatThrownBy(() ->
                assignmentService.createAssignment(
                        lesson.getId(),
                        "Title",
                        "",
                        LocalDate.now(),
                        10
                )
        )
                .isInstanceOf(jakarta.validation.ValidationException.class)
                .hasMessageContaining("description");
    }

    @Test
    void findAssignmentsByLesson() {
        assignmentService.createAssignment(
                lesson.getId(),
                "Assignment 1",
                "Description 1",
                null,
                5
        );

        assignmentService.createAssignment(
                lesson.getId(),
                "Assignment 2",
                "Description 2",
                null,
                10
        );

        List<Assignment> assignments =
                assignmentService.findByLesson(lesson.getId());

        assertThat(assignments)
                .hasSize(2)
                .extracting(Assignment::getTitle)
                .containsExactlyInAnyOrder(
                        "Assignment 1",
                        "Assignment 2"
                );
    }
}
