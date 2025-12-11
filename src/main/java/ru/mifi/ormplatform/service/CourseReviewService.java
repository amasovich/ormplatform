package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.CourseReview;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с отзывами пользователя по курсу.
 */
public interface CourseReviewService {

    CourseReview createReview(Long courseId,
                              Long studentId,
                              Integer rating,
                              String comment);

    Optional<CourseReview> findById(Long id);

    List<CourseReview> findByCourse(Long courseId);

    List<CourseReview> findByStudent(Long studentId);

    CourseReview updateReview(Long reviewId,
                              Integer rating,
                              String comment);

    void deleteReview(Long reviewId);
}
