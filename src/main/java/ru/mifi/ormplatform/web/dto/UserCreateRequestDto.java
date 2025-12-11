package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.UserRole;

/**
 * DTO-запрос для создания пользователя.
 * Используется в UserController.
 */
public class UserCreateRequestDto {

    private String name;
    private String email;
    private UserRole role;

    public UserCreateRequestDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
