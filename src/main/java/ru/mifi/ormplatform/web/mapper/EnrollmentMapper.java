package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.web.dto.EnrollmentDto;

/**
 * Маппер сущности Enrollment в DTO для REST-слоя.
 * Передаёт только безопасные и необходимые поля.
 */
@Component
public class EnrollmentMapper {

    /**
     * Преобразование JPA-сущности Enrollment → EnrollmentDto.
     *
     * @param enrollment запись о зачислении студента на курс
     * @return DTO для API
     */
    public EnrollmentDto toDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();

        dto.setId(enrollment.getId());
        dto.setEnrollDate(enrollment.getEnrollDate());

        // Курс
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
        }

        // Студент
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
            dto.setStudentName(enrollment.getStudent().getName());
        }

        // Статус лучше отдавать ENUM
        dto.setStatus(enrollment.getStatus().name());

        return dto;
    }
}
