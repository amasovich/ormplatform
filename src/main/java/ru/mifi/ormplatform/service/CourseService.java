package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с курсами.
 * Содержит операции создания, обновления, удаления и поиска.
 */
public interface CourseService {

    /**
     * Создаю новый курс.
     *
     * @param title       название курса.
     * @param description описание курса.
     * @param categoryId  идентификатор категории.
     * @param teacherId   идентификатор преподавателя (роль TEACHER).
     * @param duration    длительность курса (в часах, неделях и т.п.).
     * @param startDate   дата старта курса (может быть null).
     * @return созданный курс.
     */
    Course createCourse(String title,
                        String description,
                        Long categoryId,
                        Long teacherId,
                        Integer duration,
                        LocalDate startDate);

    /**
     * Добавляю тег к курсу.
     *
     * @param courseId идентификатор курса.
     * @param tagId    идентификатор тега.
     * @return обновлённый курс.
     */
    Course addTagToCourse(Long courseId, Long tagId);

    /**
     * Получаю курсы по категории.
     */
    List<Course> findByCategory(Long categoryId);

    /**
     * Получаю курсы, которые ведёт преподаватель.
     */
    List<Course> findByTeacher(Long teacherId);

    /**
     * Поиск курсов по части названия.
     */
    List<Course> searchByTitle(String titlePart);

    /**
     * Получаю все курсы.
     */
    List<Course> findAllCourses();

    /**
     * Поиск курса по id.
     */
    Optional<Course> findById(Long id);

    /**
     * Получаю курс по id или выбрасываю исключение.
     */
    Course getByIdOrThrow(Long id);

    /**
     * Сохраняю изменения курса.
     */
    Course save(Course course);

    /**
     * Удаляю курс.
     */
    void delete(Long id);
}
