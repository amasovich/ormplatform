package ru.mifi.ormplatform.web.dto;

import ru.mifi.ormplatform.domain.enums.UserRole;

/**
 * DTO для отображения данных пользователя во внешнем REST API.
 * Используется в ответах UserController.
 *
 * Содержит только безопасные для отдачи клиенту поля:
 *  - id
 *  - имя
 *  - email
 *  - роль (STUDENT / TEACHER / ADMIN)
 *
 * Поля, связанные с безопасностью (пароли, токены), здесь отсутствуют.
 */
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private UserRole role;

    public UserDto() {
        // Пустой конструктор необходим для Jackson
    }

    // ======================
    //        GETTERS
    // ======================

    public Long getId() {
        return id;
    }

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

    public void setId(Long id) {
        this.id = id;
    }

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
