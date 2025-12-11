package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.NotNull;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

/**
 * DTO для обновления статуса записи студента на курс.
 *
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

    /**
     * Новый статус записи.
     * Обязательно должен быть указан — иначе запрос некорректен.
     */
    @NotNull(message = "Поле status обязательно")
    private EnrollmentStatus status;

    public EnrollmentStatusUpdateDto() {
        // пустой конструктор для Jackson
    }

    public EnrollmentStatus getStatus() {
        return status;
    }

    public void setStatus(EnrollmentStatus status) {
        this.status = status;
    }
}
