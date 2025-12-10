package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с пользователями.
 * Здесь описываю основные бизнес-операции: регистрация, поиск по роли и т.п.
 */
public interface UserService {

    /**
     * Создаю нового пользователя с указанной ролью.
     * Пока без пароля и авторизации — на этом этапе нам важна только учебная модель.
     *
     * @param name имя пользователя.
     * @param email e-mail пользователя.
     * @param role роль пользователя (STUDENT, TEACHER, ADMIN).
     * @return созданный пользователь.
     */
    User createUser(String name, String email, UserRole role);

    /**
     * Ищу пользователя по идентификатору.
     *
     * @param id идентификатор пользователя.
     * @return Optional с пользователем, если найден.
     */
    Optional<User> findById(Long id);

    /**
     * Ищу пользователя по e-mail.
     *
     * @param email e-mail.
     * @return Optional с пользователем, если найден.
     */
    Optional<User> findByEmail(String email);

    /**
     * Получаю всех пользователей с указанной ролью.
     *
     * @param role роль.
     * @return список пользователей.
     */
    List<User> findByRole(UserRole role);

    /**
     * Получаю всех пользователей.
     *
     * @return список всех пользователей.
     */
    List<User> findAll();
}

