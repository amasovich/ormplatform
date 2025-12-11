package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.web.dto.ProfileDto;

/**
 * Маппер между сущностью {@link Profile} и транспортной моделью {@link ProfileDto}.
 * Отвечает за корректную и безопасную конвертацию данных профиля пользователя
 * в объект, передаваемый наружу REST-клиенту.
 *
 * <p>Методы null-safe и не вызывают NPE.</p>
 */
@Component
public class ProfileMapper {

    /**
     * Преобразует сущность {@link Profile} в DTO {@link ProfileDto}.
     * Если передан null — возвращает null.
     *
     * @param profile сущность профиля.
     * @return DTO профиля или null.
     */
    public ProfileDto toDto(Profile profile) {
        if (profile == null) {
            return null;
        }

        ProfileDto dto = new ProfileDto();
        dto.setId(profile.getId());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());

        // Безопасно экспортируем данные пользователя
        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setUserName(profile.getUser().getName());
        }

        return dto;
    }
}
