package ru.mifi.ormplatform.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CourseRepositoryIT {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void save_and_find_course_with_relations() {
        // ---------- given ----------
        Category category = new Category();
        category.setName("Java Backend");
        category = categoryRepository.save(category);

        User teacher = new User();
        teacher.setName("Test Teacher");
        teacher.setEmail("teacher@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Spring ORM");
        course.setDescription("ORM and Hibernate course");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(6);
        course.setStartDate(LocalDate.now());

        // ---------- when ----------
        Course saved = courseRepository.save(course);

        // ---------- then ----------
        assertThat(saved.getId()).isNotNull();

        Course found = courseRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();

        assertThat(found.getTitle()).isEqualTo("Spring ORM");
        assertThat(found.getCategory()).isNotNull();
        assertThat(found.getCategory().getName()).isEqualTo("Java Backend");

        assertThat(found.getTeacher()).isNotNull();
        assertThat(found.getTeacher().getRole()).isEqualTo(UserRole.TEACHER);
    }

    @Test
    void find_courses_by_category() {
        // ---------- given ----------
        Category category = new Category();
        category.setName("Databases");
        category = categoryRepository.save(category);

        User teacher = new User();
        teacher.setName("DB Teacher");
        teacher.setEmail("db@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course c1 = new Course();
        c1.setTitle("SQL Basics");
        c1.setDescription("Introductory SQL course");
        c1.setCategory(category);
        c1.setTeacher(teacher);
        c1.setDuration(4);

        Course c2 = new Course();
        c2.setTitle("Hibernate Deep Dive");
        c2.setDescription("Advanced Hibernate topics");
        c2.setCategory(category);
        c2.setTeacher(teacher);
        c2.setDuration(8);

        courseRepository.save(c1);
        courseRepository.save(c2);

        // ---------- when ----------
        List<Course> courses = courseRepository.findAllByCategory_Id(category.getId());

        // ---------- then ----------
        assertThat(courses).hasSize(2);
        assertThat(courses)
                .extracting(Course::getTitle)
                .containsExactlyInAnyOrder("SQL Basics", "Hibernate Deep Dive");
    }

    @Test
    void find_courses_by_teacher() {
        // ---------- given ----------
        Category category = new Category();
        category.setName("Spring");
        category = categoryRepository.save(category);

        User teacher = new User();
        teacher.setName("Spring Teacher");
        teacher.setEmail("spring@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Course course = new Course();
        course.setTitle("Spring Boot");
        course.setDescription("Spring Boot fundamentals");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(5);

        courseRepository.save(course);

        // ---------- when ----------
        List<Course> courses = courseRepository.findAllByTeacher_Id(teacher.getId());

        // ---------- then ----------
        assertThat(courses).hasSize(1);
        assertThat(courses.get(0).getTitle()).isEqualTo("Spring Boot");
    }
}
