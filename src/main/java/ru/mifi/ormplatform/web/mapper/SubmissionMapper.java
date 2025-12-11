package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.web.dto.SubmissionDto;

/**
 * Маппер для преобразования сущности {@link Submission}
 * в DTO-модель {@link SubmissionDto}.
 * <p>
 * Используется контроллерами и сервисами для передачи данных о
 * сдаче практических заданий без утечки JPA-сущностей в REST-слой.
 */
@Component
public class SubmissionMapper {

    /**
     * Конвертирует JPA-сущность {@link Submission} в DTO {@link SubmissionDto}.
     * <p>
     * Метод безопасен для null — при передаче null возвращает null,
     * предотвращая возможные NullPointerException в контроллерах.
     *
     * @param submission сущность отправленного решения.
     * @return DTO-представление отправки или null.
     */
    public SubmissionDto toDto(Submission submission) {
        if (submission == null) {
            return null;
        }

        SubmissionDto dto = new SubmissionDto();
        dto.setId(submission.getId());
        dto.setSubmittedAt(submission.getSubmittedAt());
        dto.setContent(submission.getContent());
        dto.setScore(submission.getScore());
        dto.setFeedback(submission.getFeedback());

        // --- Assignment section ---
        if (submission.getAssignment() != null) {
            dto.setAssignmentId(submission.getAssignment().getId());
            dto.setAssignmentTitle(submission.getAssignment().getTitle());
        }

        // --- Student section ---
        if (submission.getStudent() != null) {
            dto.setStudentId(submission.getStudent().getId());
            dto.setStudentName(submission.getStudent().getName());
        }

        return dto;
    }
}
