package ru.mifi.ormplatform.domain.enums;

/**
 * Роль пользователя в системе.
 * В БД хранится как строка в колонке role.
 */
public enum UserRole {
    STUDENT,
    TEACHER,
    ADMIN
}
