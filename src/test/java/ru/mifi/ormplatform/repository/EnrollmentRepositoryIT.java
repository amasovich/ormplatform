package ru.mifi.ormplatform.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class EnrollmentRepositoryIT {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save_and_find_enrollment() {
        // ---------- given ----------
        User student = new User();
        student.setName("Student One");
        student.setEmail("student1@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        User teacher = new User();
        teacher.setName("Teacher One");
        teacher.setEmail("teacher1@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Databases");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("PostgreSQL Basics");
        course.setDescription("Introductory SQL course");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(4);
        course = courseRepository.save(course);

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus(EnrollmentStatus.ACTIVE);

        // ---------- when ----------
        Enrollment saved = enrollmentRepository.save(enrollment);

        // ---------- then ----------
        assertThat(saved.getId()).isNotNull();

        Optional<Enrollment> found =
                enrollmentRepository.findByStudent_IdAndCourse_Id(
                        student.getId(),
                        course.getId()
                );

        assertThat(found).isPresent();
        assertThat(found.get().getStatus()).isEqualTo(EnrollmentStatus.ACTIVE);
    }

    @Test
    void find_enrollments_by_student() {
        // ---------- given ----------
        User student = new User();
        student.setName("Student Two");
        student.setEmail("student2@test.com");
        student.setRole(UserRole.STUDENT);
        student = userRepository.save(student);

        User teacher = new User();
        teacher.setName("Teacher Two");
        teacher.setEmail("teacher2@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("Java");
        category = categoryRepository.save(category);

        Course c1 = new Course();
        c1.setTitle("Java Core");
        c1.setDescription("Java language fundamentals");
        c1.setCategory(category);
        c1.setTeacher(teacher);
        c1.setDuration(6);

        Course c2 = new Course();
        c2.setTitle("Spring Boot");
        c2.setDescription("Spring Boot framework basics");
        c2.setCategory(category);
        c2.setTeacher(teacher);
        c2.setDuration(8);

        courseRepository.save(c1);
        courseRepository.save(c2);

        Enrollment e1 = new Enrollment();
        e1.setStudent(student);
        e1.setCourse(c1);
        e1.setEnrollDate(LocalDate.now());
        e1.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment e2 = new Enrollment();
        e2.setStudent(student);
        e2.setCourse(c2);
        e2.setEnrollDate(LocalDate.now());
        e2.setStatus(EnrollmentStatus.ACTIVE);

        enrollmentRepository.save(e1);
        enrollmentRepository.save(e2);


        // ---------- when ----------
        List<Enrollment> enrollments =
                enrollmentRepository.findAllByStudent_Id(student.getId());

        // ---------- then ----------
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments)
                .extracting(e -> e.getCourse().getTitle())
                .containsExactlyInAnyOrder("Java Core", "Spring Boot");
    }

    @Test
    void find_enrollments_by_course() {
        // ---------- given ----------
        User student1 = new User();
        student1.setName("Student A");
        student1.setEmail("a@test.com");
        student1.setRole(UserRole.STUDENT);
        student1 = userRepository.save(student1);

        User student2 = new User();
        student2.setName("Student B");
        student2.setEmail("b@test.com");
        student2.setRole(UserRole.STUDENT);
        student2 = userRepository.save(student2);

        User teacher = new User();
        teacher.setName("Teacher A");
        teacher.setEmail("teacher@test.com");
        teacher.setRole(UserRole.TEACHER);
        teacher = userRepository.save(teacher);

        Category category = new Category();
        category.setName("ORM");
        category = categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Hibernate");
        course.setDescription("Hibernate ORM deep dive");
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(5);
        course = courseRepository.save(course);

        Enrollment e1 = new Enrollment();
        e1.setStudent(student1);
        e1.setCourse(course);
        e1.setEnrollDate(LocalDate.now());
        e1.setStatus(EnrollmentStatus.ACTIVE);

        Enrollment e2 = new Enrollment();
        e2.setStudent(student2);
        e2.setCourse(course);
        e2.setEnrollDate(LocalDate.now());
        e2.setStatus(EnrollmentStatus.ACTIVE);

        enrollmentRepository.save(e1);
        enrollmentRepository.save(e2);


        // ---------- when ----------
        List<Enrollment> enrollments =
                enrollmentRepository.findAllByCourse_Id(course.getId());

        // ---------- then ----------
        assertThat(enrollments).hasSize(2);
        assertThat(enrollments)
                .extracting(e -> e.getStudent().getName())
                .containsExactlyInAnyOrder("Student A", "Student B");
    }
}
