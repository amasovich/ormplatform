package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.web.dto.EnrollmentDto;

/**
 * Маппер сущности {@link Enrollment} в DTO {@link EnrollmentDto}.
 * Используется REST-слоем для безопасной передачи данных о зачислении.
 *
 * <p>Метод безопасен по отношению к null: если enrollment == null,
 * возвращается null вместо выброса исключения.</p>
 */
@Component
public class EnrollmentMapper {

    /**
     * Преобразует сущность {@link Enrollment} в DTO.
     *
     * @param enrollment запись о зачислении студента. Может быть null.
     * @return DTO или null, если входной объект отсутствует.
     */
    public EnrollmentDto toDto(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        EnrollmentDto dto = new EnrollmentDto();

        dto.setId(enrollment.getId());
        dto.setEnrollDate(enrollment.getEnrollDate());

        // --- Course ---
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
        }

        // --- Student ---
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
            dto.setStudentName(enrollment.getStudent().getName());
        }

        // --- Status ---
        if (enrollment.getStatus() != null) {
            dto.setStatus(enrollment.getStatus().name());
        }

        return dto;
    }
}
