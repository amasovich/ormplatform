package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.service.ProfileService;
import ru.mifi.ormplatform.web.dto.ProfileDto;
import ru.mifi.ormplatform.web.dto.ProfileUpdateRequestDto;
import ru.mifi.ormplatform.web.mapper.ProfileMapper;

/**
 * REST-контроллер для просмотра и обновления профиля пользователя.
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService,
                             ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    /**
     * Получаю профиль по ID пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return DTO профиля.
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long userId) {

        Profile profile = profileService.getByUserId(userId);
        if (profile == null) {
            throw new EntityNotFoundException("Profile not found for userId=" + userId);
        }

        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    /**
     * Обновляю профиль пользователя.
     *
     * @param userId ID пользователя.
     * @param request DTO с обновлениями (bio, avatarUrl).
     * @return обновлённый профиль.
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ProfileDto> updateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileUpdateRequestDto request
    ) {
        Profile updated = profileService.updateProfile(
                userId,
                request.getBio(),
                request.getAvatarUrl()
        );

        return ResponseEntity.ok(profileMapper.toDto(updated));
    }
}
