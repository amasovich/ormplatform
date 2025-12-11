package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.web.dto.ProfileUpdateRequestDto;

import java.util.List;

/**
 * Сервис для работы с профилем пользователя.
 */
public interface ProfileService {

    /**
     * Получить профиль.
     */
    Profile getByUserId(Long userId);

    /**
     * Обновление профиля.
     */
    Profile updateProfile(Long userId, String bio, String avatarUrl);
}

