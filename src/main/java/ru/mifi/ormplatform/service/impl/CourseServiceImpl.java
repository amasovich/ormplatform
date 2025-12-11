package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
 * Содержит строгую валидацию, проверку ролей, нормализацию и корректное
 * использование исключений (ValidationException, EntityNotFoundException).
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

    // ============================================================================
    //                               CREATE COURSE
    // ============================================================================

    @Override
    public Course createCourse(String title,
                               String description,
                               Long categoryId,
                               Long teacherId,
                               Integer duration,
                               LocalDate startDate) {

        // -------- Валидация входных данных --------
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Course title cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Course description cannot be empty");
        }
        if (categoryId == null) {
            throw new ValidationException("categoryId is required");
        }
        if (teacherId == null) {
            throw new ValidationException("teacherId is required");
        }
        if (duration == null || duration <= 0) {
            throw new ValidationException("duration must be a positive integer");
        }

        // -------- Загрузка категории --------
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found: id=" + categoryId));

        // -------- Загрузка и проверка преподавателя --------
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + teacherId));

        if (teacher.getRole() != UserRole.TEACHER) {
            throw new ValidationException("Only TEACHER can be assigned to a course");
        }

        // -------- Нормализация --------
        String normalizedTitle = title.trim();
        String normalizedDescription = description.trim();

        // -------- Создание курса --------
        Course course = new Course();
        course.setTitle(normalizedTitle);
        course.setDescription(normalizedDescription);
        course.setCategory(category);
        course.setTeacher(teacher);
        course.setDuration(duration);
        course.setStartDate(startDate);

        return courseRepository.save(course);
    }

    // ============================================================================
    //                             ADD TAG TO COURSE
    // ============================================================================

    @Override
    public Course addTagToCourse(Long courseId, Long tagId) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + courseId));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Tag not found: id=" + tagId));

        // уже привязан — ничего не делаем
        if (course.getTags().contains(tag)) {
            return course;
        }

        course.getTags().add(tag);
        tag.getCourses().add(course); // поддерживаем bidirectional, если нужно

        return courseRepository.save(course);
    }

    // ============================================================================
    //                                   READ
    // ============================================================================

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
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + id));
    }

    // ============================================================================
    //                                   SAVE / DELETE
    // ============================================================================

    @Override
    public Course save(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + id));

        courseRepository.delete(course);
    }
}
