package ru.mifi.ormplatform.domain.enums;

/**
 * Статус участия студента в курсе.
 * В БД хранится как строка в колонке status.
 */
public enum EnrollmentStatus {
    ACTIVE,
    COMPLETED,
    CANCELLED
}
