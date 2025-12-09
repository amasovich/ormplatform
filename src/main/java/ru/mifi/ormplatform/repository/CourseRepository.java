package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Course;

import java.util.List;

/**
 * Репозиторий для курсов (courses).
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Курсы по категории.
     */
    List<Course> findAllByCategory_Id(Long categoryId);

    /**
     * Курсы, которые ведёт конкретный преподаватель.
     */
    List<Course> findAllByTeacher_Id(Long teacherId);

    /**
     * Поиск курсов по части названия (для поиска на сайте).
     */
    List<Course> findAllByTitleContainingIgnoreCase(String titlePart);
}

