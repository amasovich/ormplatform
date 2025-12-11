package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.repository.ProfileRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.ProfileService;
import ru.mifi.ormplatform.web.dto.ProfileUpdateRequestDto;

import java.util.List;

/**
 * Реализация сервиса работы с профилями пользователей.
 * Поддерживает просмотр и обновление минимального профиля:
 * bio + avatarUrl.
 */
@Service
@Transactional
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository,
                              UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Получаю профиль по ID пользователя.
     */
    @Override
    @Transactional(readOnly = true)
    public Profile getByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пользователь не найден: " + userId));

        return profileRepository.findByUser(user)
                .orElseThrow(() ->
                        new IllegalStateException("У пользователя пока нет профиля"));
    }

    /**
     * Обновляю минимальный профиль пользователя.
     * Если профиль отсутствует — создаю пустой.
     */
    @Override
    public Profile updateProfile(Long userId,
                                 String bio,
                                 String avatarUrl) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Пользователь не найден: " + userId));

        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUser(user);
                    return p;
                });

        if (bio != null) profile.setBio(bio);
        if (avatarUrl != null) profile.setAvatarUrl(avatarUrl);

        return profileRepository.save(profile);
    }
}
