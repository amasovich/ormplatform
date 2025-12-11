package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.web.dto.ProfileDto;

/**
 * Маппер между сущностью Profile и DTO.
 * Управляет тем, какие поля наружу получает REST-клиент.
 */
@Component
public class ProfileMapper {

    /**
     * Преобразую сущность Profile в DTO.
     *
     * @param profile сущность из БД.
     * @return DTO для REST-контроллера.
     */
    public ProfileDto toDto(Profile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setId(profile.getId());
        dto.setBio(profile.getBio());
        dto.setAvatarUrl(profile.getAvatarUrl());

        if (profile.getUser() != null) {
            dto.setUserId(profile.getUser().getId());
            dto.setUserName(profile.getUser().getName());
        }

        return dto;
    }
}

