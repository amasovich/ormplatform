package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

/**
 * DTO для обновления статуса записи студента на курс.
 * <p>
 * Используется преподавателем или системой для смены состояния:
 * ACTIVE → COMPLETED → CANCELLED.
 *
 * Пример запроса:
 * PUT /api/enrollments/12/status
 * {
 *   "status": "COMPLETED"
 * }
 */
public class EnrollmentStatusUpdateDto {

    private EnrollmentStatus status;

    public EnrollmentStatusUpdateDto() {
        // Пустой конструктор для Jackson
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
