package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.web.dto.AssignmentDto;

/**
 * Маппер для преобразования сущности {@link Assignment}
 * в DTO {@link AssignmentDto} для REST-слоя.
 * <p>
 * Отдаёт наружу только безопасные поля задания.
 * Информация о решениях (Submission) маппится отдельно через {@link ru.mifi.ormplatform.web.mapper.SubmissionMapper}.
 */
@Component
public class AssignmentMapper {

    /**
     * Преобразует JPA-сущность {@link Assignment} в DTO.
     *
     * @param assignment сущность задания (может быть null)
     * @return DTO или null
     */
    public AssignmentDto toDto(Assignment assignment) {
        if (assignment == null) return null;

        AssignmentDto dto = new AssignmentDto();

        dto.setId(assignment.getId());
        dto.setTitle(assignment.getTitle());
        dto.setDescription(assignment.getDescription());
        dto.setDueDate(assignment.getDueDate());
        dto.setMaxScore(assignment.getMaxScore());

        // Привязка к уроку
        if (assignment.getLesson() != null) {
            dto.setLessonId(assignment.getLesson().getId());
        }

        return dto;
    }
}
