package ru.mifi.ormplatform.web.dto;

/**
 * DTO для обновления существующего отзыва.
 * Обновляем только рейтинг и комментарий.
 */
public class CourseReviewUpdateDto {

    private Integer rating;
    private String comment;

    public CourseReviewUpdateDto() {
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
