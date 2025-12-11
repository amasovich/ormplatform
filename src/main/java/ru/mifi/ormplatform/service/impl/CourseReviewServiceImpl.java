package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.CourseReview;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.CourseReviewRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.CourseReviewService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация CourseReviewService.
 * Включает строгую валидацию входных данных, проверки ролей,
 * корректную обработку ошибок и нормализацию текстовых полей.
 */
@Service
@Transactional
public class CourseReviewServiceImpl implements CourseReviewService {

    private final CourseReviewRepository reviewRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public CourseReviewServiceImpl(CourseReviewRepository reviewRepository,
                                   CourseRepository courseRepository,
                                   UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    // ============================================================================
    //                               CREATE REVIEW
    // ============================================================================

    @Override
    public CourseReview createReview(Long courseId,
                                     Long studentId,
                                     Integer rating,
                                     String comment) {

        // ----- Валидация базовых параметров -----
        if (courseId == null) {
            throw new ValidationException("courseId is required");
        }
        if (studentId == null) {
            throw new ValidationException("studentId is required");
        }
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        // ----- Загрузка курса -----
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + courseId));

        // ----- Загрузка пользователя -----
        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + studentId));

        // ----- Проверка роли -----
        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException("Only STUDENT can leave course reviews");
        }

        // ----- Нормализация -----
        String normalizedComment = (comment != null && !comment.isBlank())
                ? comment.trim()
                : null;

        // ----- Создание отзыва -----
        CourseReview review = new CourseReview();
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(rating);
        review.setComment(normalizedComment);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }


    // ============================================================================
    //                                   READ
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseReview> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseReview> findByCourse(Long courseId) {
        return reviewRepository.findAllByCourse_Id(courseId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseReview> findByStudent(Long studentId) {
        return reviewRepository.findAllByStudent_Id(studentId);
    }


    // ============================================================================
    //                                 UPDATE
    // ============================================================================

    @Override
    public CourseReview updateReview(Long reviewId,
                                     Integer rating,
                                     String comment) {

        CourseReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Review not found: id=" + reviewId));

        // Валидация рейтинга
        if (rating == null || rating < 1 || rating > 5) {
            throw new ValidationException("Rating must be between 1 and 5");
        }

        // Нормализация
        String normalizedComment =
                (comment != null && !comment.isBlank()) ? comment.trim() : null;

        review.setRating(rating);
        review.setComment(normalizedComment);

        return reviewRepository.save(review);
    }


    // ============================================================================
    //                                DELETE
    // ============================================================================

    @Override
    public void deleteReview(Long reviewId) {
        CourseReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Review not found: id=" + reviewId));

        reviewRepository.delete(review);
    }
}
