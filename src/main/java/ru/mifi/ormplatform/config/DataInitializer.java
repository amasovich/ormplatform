package ru.mifi.ormplatform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.service.CategoryService;
import ru.mifi.ormplatform.service.CourseService;
import ru.mifi.ormplatform.service.TagService;
import ru.mifi.ormplatform.service.UserService;

import java.time.LocalDate;
import java.util.List;

/**
 * Инициализатор данных.
 * На старте приложения создаёт базовый набор пользователей, категорий, тегов и курсов.
 * Этот класс облегчит ручную проверку в DBeaver и через будущие REST-эндпоинты.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final TagService tagService;
    private final CourseService courseService;

    public DataInitializer(UserService userService,
                           CategoryService categoryService,
                           TagService tagService,
                           CourseService courseService) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.courseService = courseService;
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

        // Привязываем теги
        List<Tag> tags = List.of(tagJpa, tagHibernate, tagSpringData);
        for (Tag tag : tags) {
            courseService.addTagToCourse(course.getId(), tag.getId());
        }
    }
}

