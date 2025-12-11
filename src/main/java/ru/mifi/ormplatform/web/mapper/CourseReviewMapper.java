package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.CourseReview;
import ru.mifi.ormplatform.web.dto.CourseReviewDto;

/**
 * Маппер сущности {@link CourseReview} в DTO {@link CourseReviewDto}.
 * Используется REST-слоем для безопасной передачи отзывов студентов.
 *
 * <p>Метод toDto() безопасен к null — при отсутствии входной сущности
 * возвращает null без генерации исключений.</p>
 */
@Component
public class CourseReviewMapper {

    /**
     * Преобразует объект {@link CourseReview} в транспортный объект DTO.
     *
     * @param review сущность отзыва (может быть null)
     * @return DTO или null, если review == null
     */
    public CourseReviewDto toDto(CourseReview review) {
        if (review == null) {
            return null;
        }

        CourseReviewDto dto = new CourseReviewDto();

        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        // --- Course ---
        if (review.getCourse() != null) {
            dto.setCourseId(review.getCourse().getId());
            dto.setCourseTitle(review.getCourse().getTitle());
        }

        // --- Student ---
        if (review.getStudent() != null) {
            dto.setStudentId(review.getStudent().getId());
            dto.setStudentName(review.getStudent().getName());
        }

        return dto;
    }
}
