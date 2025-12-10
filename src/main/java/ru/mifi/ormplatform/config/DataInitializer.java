package ru.mifi.ormplatform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.entity.Question;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.QuestionType;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.service.AssignmentService;
import ru.mifi.ormplatform.service.CategoryService;
import ru.mifi.ormplatform.service.CourseService;
import ru.mifi.ormplatform.service.LessonService;
import ru.mifi.ormplatform.service.ModuleService;
import ru.mifi.ormplatform.service.QuestionService;
import ru.mifi.ormplatform.service.QuizService;
import ru.mifi.ormplatform.service.TagService;
import ru.mifi.ormplatform.service.UserService;

import java.time.LocalDate;

/**
 * Инициализатор данных.
 * На старте приложения создаёт базовый набор пользователей, категорий, тегов,
 * курсов, модулей, уроков, заданий и квиза с вопросами.
 *
 * Это упрощает ручную проверку схемы и работу будущих REST-эндпоинтов.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final CourseService courseService;
    private final ModuleService moduleService;
    private final LessonService lessonService;
    private final AssignmentService assignmentService;
    private final QuizService quizService;
    private final QuestionService questionService;

    /**
     * Конструктор с внедрением всех необходимых сервисов.
     */
    public DataInitializer(UserService userService,
                           CategoryService categoryService,
                           TagService tagService,
                           CourseService courseService,
                           ModuleService moduleService,
                           LessonService lessonService,
                           AssignmentService assignmentService,
                           QuizService quizService,
                           QuestionService questionService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.courseService = courseService;
        this.moduleService = moduleService;
        this.lessonService = lessonService;
        this.assignmentService = assignmentService;
        this.quizService = quizService;
        this.questionService = questionService;
    }

    @Override
    public void run(String... args) {
        // Простейшая защита от повторной инициализации:
        if (!userService.findAll().isEmpty()) {
            return;
        }

        // 1. Пользователи
        User teacher = userService.createUser(
                "ORM Mentor",
                "teacher@example.com",
                UserRole.TEACHER
        );

        User student = userService.createUser(
                "First Student",
                "student@example.com",
                UserRole.STUDENT
        );

        // 2. Категория
        Category category = categoryService.createCategory("Java / ORM / Hibernate");

        // 3. Теги
        Tag tagJpa = tagService.createTag("JPA");
        Tag tagHibernate = tagService.createTag("Hibernate");
        Tag tagSpringData = tagService.createTag("Spring Data JPA");

        // 4. Курс
        Course course = courseService.createCourse(
                "Практический курс по ORM и Hibernate",
                "Учебная платформа по ORM и Hibernate на Spring Boot.",
                category.getId(),
                teacher.getId(),
                "6 недель",
                LocalDate.now()
        );

        // Привязываю теги к курсу
        courseService.addTagToCourse(course.getId(), tagJpa.getId());
        courseService.addTagToCourse(course.getId(), tagHibernate.getId());
        courseService.addTagToCourse(course.getId(), tagSpringData.getId());

        // 5. Модуль
        Module module1 = moduleService.createModule(
                course.getId(),
                "Введение в ORM и JPA",
                1,
                "Основные понятия ORM, JPA и Hibernate."
        );

        // 6. Уроки
        Lesson lesson1 = lessonService.createLesson(
                module1.getId(),
                "Что такое ORM и зачем оно нужно",
                "В этом уроке я кратко объясняю идею ORM: маппинг объектов " +
                        "на строки таблиц БД, преимущества и типичные подводные камни.",
                null
        );

        Lesson lesson2 = lessonService.createLesson(
                module1.getId(),
                "JPA как спецификация и Hibernate как её реализация",
                "В этом уроке разбираю разницу между JPA (спецификация) и " +
                        "конкретными реализациями, включая Hibernate и Spring Data JPA.",
                null
        );

        // 7. Практическое задание к уроку 2
        assignmentService.createAssignment(
                lesson2.getId(),
                "Поднять простой проект на Spring Data JPA",
                "Нужно создать базовый проект Spring Boot с одной сущностью и " +
                        "реализовать CRUD-операции через Spring Data JPA.",
                null,
                10
        );

        // 8. Квиз по модулю
        Quiz quiz = quizService.createQuiz(
                course.getId(),
                module1.getId(),
                "Проверка базовых понятий ORM и JPA",
                15
        );

        // 9. Вопросы и варианты ответов
        Question q1 = questionService.createQuestion(
                quiz.getId(),
                "Что делает ORM-фреймворк в контексте приложения?",
                QuestionType.SINGLE_CHOICE
        );

        questionService.addAnswerOption(
                q1.getId(),
                "Автоматизирует маппинг объектов Java на строки таблиц БД и обратно.",
                true
        );
        questionService.addAnswerOption(
                q1.getId(),
                "Компилирует Java-код в байт-код JVM.",
                false
        );
        questionService.addAnswerOption(
                q1.getId(),
                "Управляет транзакциями операционной системы.",
                false
        );

        Question q2 = questionService.createQuestion(
                quiz.getId(),
                "Выберите корректные утверждения о JPA.",
                QuestionType.MULTIPLE_CHOICE
        );

        questionService.addAnswerOption(
                q2.getId(),
                "JPA — это спецификация, описывающая API для работы с ORM в Java.",
                true
        );
        questionService.addAnswerOption(
                q2.getId(),
                "Hibernate является одной из реализаций JPA.",
                true
        );
        questionService.addAnswerOption(
                q2.getId(),
                "JPA — это драйвер базы данных PostgreSQL.",
                false
        );

        // На этом базовая структура данных для учебного стенда сформирована.

    }
}

