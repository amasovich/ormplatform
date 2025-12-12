package ru.mifi.ormplatform.service;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CourseServiceIT {

    @Autowired private CourseService courseService;

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private CourseRepository courseRepository;

    private User teacher;
    private User student;
    private Category category;
    private Tag tag;

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
        category = new Category();
        category.setName("Programming");
        category = categoryRepository.save(category);

        // ---------- Tag ----------
        tag = new Tag();
        tag.setName("Spring");
        tag = tagRepository.save(tag);
    }

    // ---------------------------------------------------------------------
    // CREATE
    // ---------------------------------------------------------------------

    @Test
    void can_create_course_with_teacher_and_category() {
        Course course = courseService.createCourse(
                "Spring Boot Course",
                "Learn Spring Boot",
                category.getId(),
                teacher.getId(),
                6,
                LocalDate.now()
        );

        assertThat(course.getId()).isNotNull();
        assertThat(course.getTeacher().getId()).isEqualTo(teacher.getId());
        assertThat(course.getCategory().getId()).isEqualTo(category.getId());
    }

    @Test
    void cannot_create_course_with_non_teacher() {
        assertThrows(ValidationException.class, () ->
                courseService.createCourse(
                        "Invalid Course",
                        "Should fail",
                        category.getId(),
                        student.getId(),
                        4,
                        LocalDate.now()
                )
        );
    }

    // ---------------------------------------------------------------------
    // FIND
    // ---------------------------------------------------------------------

    @Test
    void can_find_courses_by_category() {
        courseService.createCourse(
                "Course 1",
                "Desc",
                category.getId(),
                teacher.getId(),
                5,
                LocalDate.now()
        );

        List<Course> courses =
                courseService.findByCategory(category.getId());

        assertThat(courses).hasSize(1);
    }

    @Test
    void can_find_courses_by_teacher() {
        courseService.createCourse(
                "Course 2",
                "Desc",
                category.getId(),
                teacher.getId(),
                5,
                LocalDate.now()
        );

        List<Course> courses =
                courseService.findByTeacher(teacher.getId());

        assertThat(courses).hasSize(1);
    }

    @Test
    void can_search_courses_by_title() {
        courseService.createCourse(
                "Java ORM",
                "Hibernate course",
                category.getId(),
                teacher.getId(),
                5,
                LocalDate.now()
        );

        List<Course> courses =
                courseService.searchByTitle("orm");

        assertThat(courses).hasSize(2);
    }

    // ---------------------------------------------------------------------
    // TAGS
    // ---------------------------------------------------------------------

    @Test
    void can_add_tag_to_course() {
        Course course = courseService.createCourse(
                "Tagged Course",
                "With tag",
                category.getId(),
                teacher.getId(),
                4,
                LocalDate.now()
        );

        Course updated =
                courseService.addTagToCourse(course.getId(), tag.getId());

        assertThat(updated.getTags()).hasSize(1);
        assertThat(updated.getTags().iterator().next().getName()).isEqualTo("Spring");
    }

    // ---------------------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------------------

    @Test
    void can_delete_course() {
        Course course = courseService.createCourse(
                "To Delete",
                "Remove me",
                category.getId(),
                teacher.getId(),
                3,
                LocalDate.now()
        );

        Long courseId = course.getId();
        courseService.delete(courseId);

        assertThat(courseRepository.findById(courseId)).isEmpty();
    }
}
