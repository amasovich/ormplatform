package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с курсами.
 */
public interface CourseService {

    /**
     * Создаю новый курс.
     *
     * @param title название курса.
     * @param description описание курса.
     * @param categoryId идентификатор категории.
     * @param teacherId идентификатор преподавателя (пользователь с ролью TEACHER).
     * @param duration строковое описание длительности.
     * @param startDate дата старта курса (может быть null).
     * @return созданный курс.
     */
    Course createCourse(String title,
                        String description,
                        Long categoryId,
                        Long teacherId,
                        String duration,
                        LocalDate startDate);

    /**
     * Добавляю тег к курсу (через таблицу course_tag).
     *
     * @param courseId идентификатор курса.
     * @param tagId идентификатор тега.
     * @return обновлённый курс.
     */
    Course addTagToCourse(Long courseId, Long tagId);

    /**
     * Курсы по категории.
     *
     * @param categoryId идентификатор категории.
     * @return список курсов.
     */
    List<Course> findByCategory(Long categoryId);

    /**
     * Курсы для конкретного преподавателя.
     *
     * @param teacherId идентификатор преподавателя.
     * @return список курсов.
     */
    List<Course> findByTeacher(Long teacherId);

    /**
     * Поиск курсов по части названия.
     *
     * @param titlePart часть названия.
     * @return список курсов.
     */
    List<Course> searchByTitle(String titlePart);

    /**
     * Возвращаю все курсы в системе.
     *
     * @return список курсов.
     */
    List<Course> findAllCourses();

    /**
     * Получаю курс по идентификатору.
     *
     * @param id идентификатор курса.
     * @return Optional с курсом, если найден.
     */
    Optional<Course> findById(Long id);

    /**
     * Получаю курс по идентификатору или бросаю IllegalArgumentException,
     * если курс не найден. Этот метод удобно использовать в REST-слое.
     *
     * @param id идентификатор курса.
     * @return найденный курс.
     */
    Course getByIdOrThrow(Long id);

}

