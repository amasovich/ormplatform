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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class QuizServiceIT {

    @Autowired private QuizService quizService;

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private ModuleRepository moduleRepository;
    @Autowired private QuizRepository quizRepository;

    private Course course;
    private Module module;

    @BeforeEach
    void setUp() {
        // ---------- Teacher ----------
        User teacher = new User();
        teacher.setName("Teacher");
        teacher.setEmail("teacher@quiz.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        // ---------- Category ----------
        Category category = new Category();
        category.setName("Testing");
        category = categoryRepository.save(category);

        // ---------- Course ----------
        course = new Course();
        course.setTitle("Quiz Course");
        course.setDescription("Course with quiz");
        course.setTeacher(teacher);
        course.setCategory(category);
        course.setDuration(4);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);

        // ---------- Module ----------
        module = new Module();
        module.setCourse(course);
        module.setTitle("Module 1");
        module.setOrderIndex(1);
        module.setDescription("Module for quiz");
        module = moduleRepository.save(module);
    }

    // ---------------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------------

    @Test
    void can_create_quiz_for_module_and_course() {
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Intro Quiz",
                10
        );

        assertThat(quiz.getId()).isNotNull();
        assertThat(quiz.getModule().getId()).isEqualTo(module.getId());
        assertThat(quiz.getTitle()).isEqualTo("Intro Quiz");
    }

    @Test
    void cannot_create_second_quiz_for_same_module() {
        quizService.createQuiz(
                course.getId(),
                module.getId(),
                "First Quiz",
                10
        );

        assertThrows(ValidationException.class, () ->
                quizService.createQuiz(
                        course.getId(),
                        module.getId(),
                        "Second Quiz",
                        15
                )
        );
    }

    @Test
    void cannot_create_quiz_if_module_not_belongs_to_course() {
        // создаём другой курс
        Course anotherCourse = new Course();
        anotherCourse.setTitle("Another");
        anotherCourse.setDescription("Another course");
        anotherCourse.setTeacher(course.getTeacher());
        anotherCourse.setCategory(course.getCategory());
        anotherCourse.setDuration(3);
        anotherCourse.setStartDate(LocalDate.now());

        anotherCourse = courseRepository.save(anotherCourse);

        final Long anotherCourseId = anotherCourse.getId();

        assertThrows(ValidationException.class, () ->
                quizService.createQuiz(
                        anotherCourseId,
                        module.getId(),
                        "Invalid Quiz",
                        10
                )
        );
    }

    // ---------------------------------------------------------------------
    // FIND
    // ---------------------------------------------------------------------

    @Test
    void can_find_quiz_by_id() {
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Find Me",
                12
        );

        Optional<Quiz> found = quizService.findById(quiz.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Find Me");
    }

    @Test
    void can_find_quiz_by_module() {
        quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Module Quiz",
                10
        );

        Optional<Quiz> found = quizService.findByModule(module.getId());

        assertThat(found).isPresent();
    }

    @Test
    void can_find_quizzes_by_course() {
        quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Course Quiz",
                10
        );

        List<Quiz> quizzes = quizService.findByCourse(course.getId());

        assertThat(quizzes).hasSize(1);
    }

    // ---------------------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------------------

    @Test
    void can_update_quiz() {
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module.getId(),
                "Old Title",
                10
        );

        Quiz updated = quizService.updateQuiz(
                quiz.getId(),
                "New Title",
                20
        );

        assertThat(updated.getTitle()).isEqualTo("New Title");
        assertThat(updated.getTimeLimit()).isEqualTo(20);
    }

    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------

    @Test
    void can_delete_quiz() {
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module.getId(),
                "To Delete",
                10
        );

        Long quizId = quiz.getId();
        quizService.deleteQuiz(quizId);

        assertThat(quizRepository.findById(quizId)).isEmpty();
    }
}
