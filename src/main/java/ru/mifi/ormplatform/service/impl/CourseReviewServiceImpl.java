package ru.mifi.ormplatform.service.impl;

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
 * Реализация сервиса для работы с отзывами пользователя по курсу.
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

    @Override
    public CourseReview createReview(Long courseId,
                                     Long studentId,
                                     Integer rating,
                                     String comment) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пользователь с id=" + studentId + " не найден"));

        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalStateException("Только STUDENT может оставлять отзыв");
        }

        CourseReview review = new CourseReview();
        review.setCourse(course);
        review.setStudent(student);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    @Override
    public Optional<CourseReview> findById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<CourseReview> findByCourse(Long courseId) {
        return reviewRepository.findAllByCourse_Id(courseId);
    }

    @Override
    public List<CourseReview> findByStudent(Long studentId) {
        return reviewRepository.findAllByStudent_Id(studentId);
    }

    @Override
    public CourseReview updateReview(Long reviewId,
                                     Integer rating,
                                     String comment) {

        CourseReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Отзыв с id=" + reviewId + " не найден"));

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        CourseReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Отзыв с id=" + reviewId + " не найден"));

        reviewRepository.delete(review);
    }

}
