package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.web.dto.AssignmentDto;
import ru.mifi.ormplatform.web.dto.SubmissionDto;

/**
 * Маппер между сущностями заданий/решений и их DTO.
 * <p>
 * Здесь я собираю только те поля, которые действительно нужны REST-клиенту,
 * и не «вываливаю» наружу всю JPA-сущность целиком.
 */
@Component
public class AssignmentMapper {

    /**
     * Преобразую сущность задания в DTO.
     *
     * @param assignment исходная JPA-сущность.
     * @return DTO для ответа контроллера.
     */
    public AssignmentDto toDto(Assignment assignment) {
        AssignmentDto dto = new AssignmentDto();
        dto.setId(assignment.getId());
        if (assignment.getLesson() != null) {
            dto.setLessonId(assignment.getLesson().getId());
        }
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());
        return dto;
    }

    /**
     * Преобразую сущность отправленного решения в DTO.
     *
     * @param submission исходная JPA-сущность.
     * @return DTO для ответа контроллера.
     */
    public SubmissionDto toDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();
        dto.setId(submission.getId());

        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
            dto.setAssignmentTitle(submission.getAssignment().getTitle());
        }

        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());

        return dto;
    }
}
