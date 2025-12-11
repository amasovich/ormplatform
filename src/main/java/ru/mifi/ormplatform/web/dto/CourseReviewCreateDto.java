package ru.mifi.ormplatform.web.dto;

/**
 * DTO для создания нового отзыва о курсе.
 * От студента принимаются только rating и comment.
 */
public class CourseReviewCreateDto {

    private Long studentId;
    private Integer rating;
    private String comment;

    public CourseReviewCreateDto() {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
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
