package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.CategoryRepository;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.TagRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.CourseService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса курсов.
 */
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             CategoryRepository categoryRepository,
                             UserRepository userRepository,
                             TagRepository tagRepository) {
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public Course createCourse(String title,
                               String description,
                               Long categoryId,
                               Long teacherId,
                               Integer duration,
                               LocalDate startDate) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Category with id=" + categoryId + " not found"));

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Teacher with id=" + teacherId + " not found"));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new IllegalArgumentException(
                    "User with id=" + teacherId + " is not a TEACHER");
        }

        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setCategory(category);
        course.setTeacher(teacher);

        // теперь duration — Integer
        course.setDuration(duration);
        course.setStartDate(startDate);

        return courseRepository.save(course);
    }

    @Override
    public Course addTagToCourse(Long courseId, Long tagId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Course with id=" + courseId + " not found"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tag with id=" + tagId + " not found"));

        if (course.getTags().contains(tag)) {
            return course;
        }

        course.getTags().add(tag);
        // При необходимости можно добавить bidirectional-связь:
        tag.getCourses().add(course);

        return courseRepository.save(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findByCategory(Long categoryId) {
        return courseRepository.findAllByCategory_Id(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findByTeacher(Long teacherId) {
        return courseRepository.findAllByTeacher_Id(teacherId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> searchByTitle(String titlePart) {
        return courseRepository.findAllByTitleContainingIgnoreCase(titlePart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Course getByIdOrThrow(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + id + " не найден"));
    }

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + id + " не найден"));

        courseRepository.delete(course);
    }

}

