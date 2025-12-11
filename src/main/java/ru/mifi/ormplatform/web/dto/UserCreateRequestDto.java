package ru.mifi.ormplatform.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.mifi.ormplatform.domain.enums.UserRole;

/**
 * DTO-запрос для создания нового пользователя.
 * Используется в UserController при POST /api/users.
 *
 * Здесь обязательно валидируем:
 *  - имя — не пустое
 *  - email — корректный формат
 *  - роль — обязательна и должна быть STUDENT / TEACHER / ADMIN
 */
public class UserCreateRequestDto {

    @NotBlank(message = "Имя пользователя обязательно")
    private String name;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    @NotNull(message = "Роль пользователя обязательна")
    private UserRole role;

    public UserCreateRequestDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public UserRole getRole() {
        return role;
    }

    // ======================
    //        SETTERS
    // ======================

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
