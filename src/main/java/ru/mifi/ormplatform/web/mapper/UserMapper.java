package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.web.dto.UserDto;

/**
 * Маппер для преобразования сущности {@link User}
 * в DTO-модель {@link UserDto}.
 * <p>
 * Используется контроллерами и сервисами для возвращения
 * данных пользователя наружу без JPA-сущностей.
 */
@Component
public class UserMapper {

    /**
     * Конвертирует сущность {@link User} в {@link UserDto}.
     * <p>
     * Метод безопасен к null:
     * если входной объект равен null — возвращает null,
     * чтобы избежать NullPointerException.
     *
     * @param user сущность пользователя.
     * @return DTO-представление пользователя.
     */
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
