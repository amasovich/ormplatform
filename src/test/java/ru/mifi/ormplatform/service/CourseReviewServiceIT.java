package ru.mifi.ormplatform.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.*;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.*;
import jakarta.validation.ValidationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class CourseReviewServiceIT {

    @Autowired private CourseReviewService reviewService;

    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;

    private Course course;
    private User student;

    @BeforeEach
    void setUp() {
        User teacher = new User();
        teacher.setName("Teacher");
        teacher.setEmail("teacher@review.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        student = new User();
        student.setName("Student");
        student.setEmail("student@review.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        Category category = new Category();
        category.setName("Reviews");
        category = categoryRepository.save(category);

        course = new Course();
        course.setTitle("Review Course");
        course.setDescription("Course with reviews");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(5);
        course.setStartDate(LocalDate.now());
        course = courseRepository.save(course);
    }

    @Test
    void student_can_create_review() {
        CourseReview review = reviewService.createReview(
                course.getId(),
                student.getId(),
                5,
                "Great course"
        );

        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getComment()).isEqualTo("Great course");
    }

    @Test
    void can_find_reviews_by_course() {
        reviewService.createReview(course.getId(), student.getId(), 4, "Nice");

        assertThat(reviewService.findByCourse(course.getId())).hasSize(1);
    }

    @Test
    void only_student_can_create_review() {
        User teacher = userRepository.findAll()
                .stream()
                .filter(u -> u.getRole() == UserRole.TEACHER)
                .findFirst()
                .orElseThrow();

        assertThrows(ValidationException.class, () ->
                reviewService.createReview(
                        course.getId(),
                        teacher.getId(),
                        5,
                        "Hack"
                )
        );
    }
}
