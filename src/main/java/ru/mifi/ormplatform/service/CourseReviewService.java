package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.CourseReview;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с отзывами пользователя о курсах.
 * Содержит операции создания, обновления, удаления и выборки отзывов.
 */
public interface CourseReviewService {

    /**
     * Создаю новый отзыв студента о курсе.
     *
     * @param courseId  идентификатор курса.
     * @param studentId идентификатор студента (роль STUDENT).
     * @param rating    оценка от 1 до 5.
     * @param comment   текст комментария (необязательно).
     * @return созданный отзыв.
     */
    CourseReview createReview(Long courseId,
                              Long studentId,
                              Integer rating,
                              String comment);

    /**
     * Получаю отзыв по id.
     */
    Optional<CourseReview> findById(Long id);

    /**
     * Все отзывы по одному курсу.
     */
    List<CourseReview> findByCourse(Long courseId);

    /**
     * Все отзывы конкретного студента.
     */
    List<CourseReview> findByStudent(Long studentId);

    /**
     * Обновляю рейтинг и комментарий отзыва.
     */
    CourseReview updateReview(Long reviewId,
                              Integer rating,
                              String comment);

    /**
     * Удаляю отзыв.
     */
    void deleteReview(Long reviewId);
}
