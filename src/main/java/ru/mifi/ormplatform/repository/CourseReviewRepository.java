package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.CourseReview;

import java.util.List;

/**
 * Репозиторий для отзывов о курсах (course_review).
 */
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {

    List<CourseReview> findAllByCourse_Id(Long courseId);

    List<CourseReview> findAllByStudent_Id(Long studentId);
}

