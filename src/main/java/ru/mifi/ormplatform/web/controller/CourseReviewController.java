package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.CourseReview;
import ru.mifi.ormplatform.service.CourseReviewService;
import ru.mifi.ormplatform.web.dto.CourseReviewCreateDto;
import ru.mifi.ormplatform.web.dto.CourseReviewDto;
import ru.mifi.ormplatform.web.dto.CourseReviewUpdateDto;
import ru.mifi.ormplatform.web.mapper.CourseReviewMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с отзывами о курсах.
 *
 * Поддерживает полный CRUD:
 * <ul>
 *     <li>Создание отзыва студентом</li>
 *     <li>Получение отзывов курса</li>
 *     <li>Получение отзывов студента</li>
 *     <li>Редактирование отзыва</li>
 *     <li>Удаление отзыва</li>
 * </ul>
 */
@RestController
@RequestMapping("/api")
public class CourseReviewController {

    private final CourseReviewService reviewService;
    private final CourseReviewMapper reviewMapper;

    public CourseReviewController(CourseReviewService reviewService,
                                  CourseReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    // =====================================================================
    //                             CREATE REVIEW
    // =====================================================================

    /**
     * Создать отзыв студента о курсе.
     *
     * Пример запроса:
     * POST /api/courses/1/reviews
     * {
     *   "studentId": 2,
     *   "rating": 5,
     *   "comment": "Отличный курс!"
     * }
     *
     * @param courseId ID курса
     * @param request  DTO с параметрами отзыва
     * @return созданный отзыв
     */
    @PostMapping("/courses/{courseId}/reviews")
    public ResponseEntity<CourseReviewDto> createReview(
            @PathVariable Long courseId,
            @RequestBody CourseReviewCreateDto request) {

        CourseReview review = reviewService.createReview(
                courseId,
                request.getStudentId(),
                request.getRating(),
                request.getComment()
        );

        return ResponseEntity.created(
                URI.create("/api/reviews/" + review.getId())
        ).body(reviewMapper.toDto(review));
    }

    // =====================================================================
    //                             GET REVIEWS
    // =====================================================================

    /**
     * Получить все отзывы по курсу.
     *
     * GET /api/courses/{courseId}/reviews
     *
     * @param courseId ID курса
     * @return список отзывов
     */
    @GetMapping("/courses/{courseId}/reviews")
    public ResponseEntity<List<CourseReviewDto>> getReviewsByCourse(
            @PathVariable Long courseId) {

        List<CourseReviewDto> result = reviewService.findByCourse(courseId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получить все отзывы студента.
     *
     * GET /api/students/{studentId}/reviews
     *
     * @param studentId ID студента
     * @return список отзывов студента
     */
    @GetMapping("/students/{studentId}/reviews")
    public ResponseEntity<List<CourseReviewDto>> getReviewsByStudent(
            @PathVariable Long studentId) {

        List<CourseReviewDto> result = reviewService.findByStudent(studentId)
                .stream()
                .map(reviewMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    // =====================================================================
    //                             UPDATE REVIEW
    // =====================================================================

    /**
     * Обновить существующий отзыв пользователя.
     *
     * PUT /api/reviews/{reviewId}
     *
     * @param reviewId ID отзыва
     * @param request  DTO с новыми полями
     * @return обновлённый отзыв
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<CourseReviewDto> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CourseReviewUpdateDto request) {

        CourseReview updated = reviewService.updateReview(
                reviewId,
                request.getRating(),
                request.getComment()
        );

        return ResponseEntity.ok(reviewMapper.toDto(updated));
    }

    // =====================================================================
    //                             DELETE REVIEW
    // =====================================================================

    /**
     * Удалить отзыв.
     *
     * DELETE /api/reviews/{reviewId}
     *
     * @param reviewId ID отзыва
     * @return статус 204 NO CONTENT
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
