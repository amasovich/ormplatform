package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Profile;

import java.util.Optional;

/**
 * Сервис для работы с профилем пользователя.
 */
public interface ProfileService {

    /**
     * Создать профиль для пользователя.
     *
     * @param userId ID пользователя.
     * @param bio биография (описание).
     * @param avatarUrl ссылка на аватар.
     * @return созданный профиль.
     */
    Profile createProfile(Long userId, String bio, String avatarUrl);

    /**
     * Обновление профиля.
     */
    Profile updateProfile(Long userId, String bio, String avatarUrl);

    /**
     * Получить профиль пользователя.
     */
    Optional<Profile> findByUserId(Long userId);

    /**
     * Получить или бросить исключение.
     */
    Profile getByUserIdOrThrow(Long userId);
}
