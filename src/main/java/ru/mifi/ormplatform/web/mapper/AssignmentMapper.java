package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.web.dto.AssignmentDto;
import ru.mifi.ormplatform.web.dto.SubmissionDto;

/**
 * Маппер между сущностями Assignment / Submission и DTO слоя.
 * Явно контролирует, какие данные выходят наружу.
 */
@Component
public class AssignmentMapper {

    /**
     * Преобразование JPA-сущности Assignment → AssignmentDto.
     *
     * @param assignment сущность задания
     * @return DTO с безопасным набором полей
     */
    public AssignmentDto toDto(Assignment assignment) {
        AssignmentDto dto = new AssignmentDto();

        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());

        if (assignment.getLesson() != null) {
            dto.setLessonId(assignment.getLesson().getId());
        }

        return dto;
    }

    /**
     * Преобразование JPA-сущности Submission → SubmissionDto.
     *
     * @param submission сущность отправленного решения
     * @return DTO с метаданными отправки и оценки
     */
    public SubmissionDto toDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();

        dto.setId(submission.getId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());

        // Assignment fields
        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
            dto.setAssignmentTitle(submission.getAssignment().getTitle());
        }

        // Student fields
        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        return dto;
    }
}
