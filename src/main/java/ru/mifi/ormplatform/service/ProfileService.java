package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Profile;

/**
 * Сервис для работы с профилем пользователя.
 * Поддерживает получение и обновление минимального профиля.
 */
public interface ProfileService {

    /**
     * Получаю профиль пользователя по его идентификатору.
     * Если профиль отсутствует — выбрасывается исключение.
     *
     * @param userId идентификатор пользователя.
     * @return профиль пользователя.
     */
    Profile getByUserId(Long userId);

    /**
     * Обновляю профиль пользователя.
     * Если профиль отсутствует — создаётся новый.
     *
     * @param userId    идентификатор пользователя.
     * @param bio       текстовая информация о пользователе.
     * @param avatarUrl URL аватара.
     * @return сохранённый профиль.
     */
    Profile updateProfile(Long userId, String bio, String avatarUrl);
}
