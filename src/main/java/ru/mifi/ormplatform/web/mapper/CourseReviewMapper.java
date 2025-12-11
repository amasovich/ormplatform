package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.CourseReview;
import ru.mifi.ormplatform.web.dto.CourseReviewDto;

/**
 * Маппер для преобразования сущности CourseReview в DTO.
 */
@Component
public class CourseReviewMapper {

    /**
     * Преобразую JPA-сущность CourseReview в REST DTO.
     *
     * @param review сущность отзыва
     * @return DTO для передачи наружу
     */
    public CourseReviewDto toDto(CourseReview review) {
        CourseReviewDto dto = new CourseReviewDto();

        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());

        if (review.getCourse() != null) {
            dto.setCourseId(review.getCourse().getId());
            dto.setCourseTitle(review.getCourse().getTitle());
        }

        if (review.getStudent() != null) {
            dto.setStudentId(review.getStudent().getId());
            dto.setStudentName(review.getStudent().getName());
        }

        return dto;
    }
}
