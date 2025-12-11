package ru.mifi.ormplatform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;
import ru.mifi.ormplatform.domain.enums.QuestionType;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Инициализатор данных.
 * Создаёт базовый набор пользователей, профилей, курсов, модулей, уроков,
 * заданий, квизов, вопросов, вариантов и тестовых прохождений.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ProfileService profileService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final CourseService courseService;
    private final ModuleService moduleService;
    private final LessonService lessonService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    private final QuestionService questionService;
    private final QuizSubmissionService quizSubmissionService;
    private final EnrollmentService enrollmentService;

    public DataInitializer(UserService userService,
                           ProfileService profileService,
                           CategoryService categoryService,
                           TagService tagService,
                           CourseService courseService,
                           ModuleService moduleService,
                           LessonService lessonService,
                           AssignmentService assignmentService,
                           QuizService quizService,
                           QuestionService questionService,
                           QuizSubmissionService quizSubmissionService,
                           EnrollmentService enrollmentService) {

        this.userService = userService;
        this.profileService = profileService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.courseService = courseService;
        this.moduleService = moduleService;
        this.lessonService = lessonService;
        this.assignmentService = assignmentService;
        this.quizService = quizService;
        this.questionService = questionService;
        this.quizSubmissionService = quizSubmissionService;
        this.enrollmentService = enrollmentService;
    }

    @Override
    public void run(String... args) {

        if (!userService.findAll().isEmpty()) {
            return;
        }

        // ======================================================
        // 1. USERS + PROFILES
        // ======================================================
        User teacher = userService.createUser(
                "ORM Mentor", "teacher@example.com", UserRole.TEACHER
        );
        profileService.updateProfile(teacher.getId(),
                "Senior Java / ORM Mentor", "https://example.com/avatar-teacher.png");

        User student1 = userService.createUser(
                "First Student", "student@example.com", UserRole.STUDENT
        );
        profileService.updateProfile(student1.getId(),
                "Beginning Java Developer", "https://example.com/avatar-student1.png");

        User student2 = userService.createUser(
                "Second Student", "student2@example.com", UserRole.STUDENT
        );
        profileService.updateProfile(student2.getId(),
                "Junior Java Enthusiast", "https://example.com/avatar-student2.png");

        // ======================================================
        // 2. CATEGORY
        // ======================================================
        Category category = categoryService.createCategory("Java / ORM / Hibernate");

        // ======================================================
        // 3. TAGS
        // ======================================================
        Tag tagJpa = tagService.createTag("JPA");
        Tag tagHibernate = tagService.createTag("Hibernate");
        Tag tagSpringData = tagService.createTag("Spring Data JPA");
        Tag tagQuery = tagService.createTag("HQL");
        Tag tagPerformance = tagService.createTag("Performance Tuning");

        // ======================================================
        // 4. COURSE
        // ======================================================
        Course course = courseService.createCourse(
                "Практический курс по ORM и Hibernate",
                "Учебная платформа по ORM и Hibernate на Spring Boot.",
                category.getId(),
                teacher.getId(),
                6,
                LocalDate.now()
        );

        courseService.addTagToCourse(course.getId(), tagJpa.getId());
        courseService.addTagToCourse(course.getId(), tagHibernate.getId());
        courseService.addTagToCourse(course.getId(), tagSpringData.getId());

        // ======================================================
        // 5. MODULE
        // ======================================================
        Module module1 = moduleService.createModule(
                course.getId(),
                "Введение в ORM и JPA",
                1,
                "Основные понятия ORM, JPA и Hibernate."
        );

        // ======================================================
        // 6. LESSONS
        // ======================================================
        Lesson lesson1 = lessonService.createLesson(
                module1.getId(),
                "Что такое ORM и зачем оно нужно",
                "Идея ORM, преимущества и типичные подводные камни.",
                null
        );

        Lesson lesson2 = lessonService.createLesson(
                module1.getId(),
                "JPA как спецификация и Hibernate как её реализация",
                "Разница между JPA и конкретными реализациями.",
                null
        );

        // ======================================================
        // 7. ASSIGNMENTS
        // ======================================================
        Assignment ass1 = assignmentService.createAssignment(
                lesson2.getId(),
                "Поднять простой проект на Spring Data JPA",
                "Создать базовый проект Spring Boot с сущностью и CRUD.",
                LocalDate.now().plusDays(7),
                10
        );

        // ======================================================
        // 8. QUIZ
        // ======================================================
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module1.getId(),
                "Проверка базовых понятий ORM и JPA",
                15
        );

        // ======================================================
        // 9. QUESTIONS + ANSWERS
        // ------------------------------------------------------
        Question q1 = questionService.createQuestion(
                quiz.getId(),
                "Что делает ORM-фреймворк?",
                QuestionType.SINGLE_CHOICE
        );
        questionService.addAnswerOption(q1.getId(),
                "Автоматизирует маппинг объектов Java и таблиц БД.", true);
        questionService.addAnswerOption(q1.getId(),
                "Компилирует Java-код.", false);
        questionService.addAnswerOption(q1.getId(),
                "Управляет драйверами ОС.", false);

        Question q2 = questionService.createQuestion(
                quiz.getId(),
                "Выберите верные утверждения о JPA",
                QuestionType.MULTIPLE_CHOICE
        );
        questionService.addAnswerOption(q2.getId(),
                "JPA — спецификация ORM в Java.", true);
        questionService.addAnswerOption(q2.getId(),
                "Hibernate — реализация JPA.", true);
        questionService.addAnswerOption(q2.getId(),
                "JPA — драйвер PostgreSQL.", false);

        // ======================================================
        // 10. ENROLLMENT
        // ======================================================
        enrollmentService.enrollStudent(course.getId(), student1.getId());
        Enrollment enrollment2 =
                enrollmentService.enrollStudent(course.getId(), student2.getId());
        enrollmentService.updateStatus(enrollment2.getId(), EnrollmentStatus.ACTIVE);

        // ======================================================
        // 11. QUIZ SUBMISSION (пример)
        // ======================================================
        quizSubmissionService.evaluateAndSaveSubmission(
                quiz.getId(),
                student1.getId(),
                Map.of(
                        q1.getId(), q1.getOptions().get(0).getId(),
                        q2.getId(), q2.getOptions().get(0).getId()
                ),
                LocalDateTime.now()
        );

        // ======================================================
        // 12. COURSE REVIEW
        // ======================================================
        CourseReview review = new CourseReview();
        review.setCourse(course);
        review.setStudent(student1);
        review.setRating(5);
        review.setComment("Отличный курс, всё понятно!");
        review.setCreatedAt(LocalDateTime.now());

        // Будет сохранён JPA каскадом или можно вызвать сервис при необходимости
    }
}
