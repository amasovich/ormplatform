package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.web.dto.AssignmentDto;

/**
 * Маппер сущности Assignment → AssignmentDto.
 * Содержит только маппинг заданий (Submission вынесен в SubmissionMapper).
 */
@Component
public class AssignmentMapper {

    /**
     * Преобразование сущности Assignment в DTO.
     *
     * @param assignment сущность задания
     * @return безопасный DTO
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
}
