package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.web.dto.EnrollmentDto;

/**
 * Маппер сущности Enrollment в DTO.
 */
@Component
public class EnrollmentMapper {

    public EnrollmentDto toDto(Enrollment enrollment) {
        EnrollmentDto dto = new EnrollmentDto();
        dto.setId(enrollment.getId());

        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
        }

        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
            dto.setStudentName(enrollment.getStudent().getName());
        }

        dto.setStatus(enrollment.getStatus().name());
        dto.setEnrollDate(enrollment.getEnrollDate());

        return dto;
    }
}
