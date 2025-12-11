package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.web.dto.SubmissionDto;

/**
 * Маппер для преобразования сущности {@link Submission}
 * в транспортный объект {@link SubmissionDto}.
 *
 * <p>
 * Вынесен в отдельный класс, чтобы разделить ответственность
 * между логикой заданий (AssignmentMapper) и логикой отправок решений.
 */
@Component
public class SubmissionMapper {

    /**
     * Преобразую отправку задания (Submission) в DTO.
     *
     * @param submission исходная JPA-сущность отправки.
     * @return DTO для REST-слоя.
     */
    public SubmissionDto toDto(Submission submission) {
        SubmissionDto dto = new SubmissionDto();

        dto.setId(submission.getId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());

        // --- Assignment ---
        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
            dto.setAssignmentTitle(submission.getAssignment().getTitle());
        }

        // --- Student ---
        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        return dto;
    }
}
