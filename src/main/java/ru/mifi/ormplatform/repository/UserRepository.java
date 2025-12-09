package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с пользователями (таблица users).
 * Здесь будут запросы по e-mail, ролям и т.п.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Ищу пользователя по e-mail.
     * Предполагаю, что e-mail уникален.
     *
     * @param email e-mail пользователя.
     * @return Optional с пользователем, если найден.
     */
    Optional<User> findByEmail(String email);

    /**
     * Получаю пользователей по роли.
     * Например, всех студентов или всех преподавателей.
     *
     * @param role роль пользователя.
     * @return список пользователей с указанной ролью.
     */
    List<User> findAllByRole(UserRole role);
}

