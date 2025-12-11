package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.service.ProfileService;
import ru.mifi.ormplatform.web.dto.ProfileDto;
import ru.mifi.ormplatform.web.dto.ProfileUpdateRequestDto;
import ru.mifi.ormplatform.web.mapper.ProfileMapper;

import java.util.List;
import java.util.stream.Collectors;

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
        return ResponseEntity.ok(
                profileMapper.toDto(profileService.getByUserId(userId))
        );
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
            @RequestBody ProfileUpdateRequestDto request
    ) {
        return ResponseEntity.ok(
                profileMapper.toDto(
                        profileService.updateProfile(
                                userId,
                                request.getBio(),
                                request.getAvatarUrl()
                        )
                )
        );
    }
}
