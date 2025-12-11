package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.repository.ProfileRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.ProfileService;

import java.util.Optional;

/**
 * Реализация сервиса для работы с профилем пользователя.
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

    @Override
    public Profile createProfile(Long userId, String bio, String avatarUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пользователь с id=" + userId + " не найден"));

        if (user.getProfile() != null) {
            throw new IllegalStateException("Профиль для этого пользователя уже существует");
        }

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setBio(bio);
        profile.setAvatarUrl(avatarUrl);

        return profileRepository.save(profile);
    }

    @Override
    public Profile updateProfile(Long userId, String bio, String avatarUrl) {
        Profile profile = profileRepository.findByUser(
                userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Пользователь с id=" + userId + " не найден"))
        ).orElseThrow(() -> new IllegalStateException(
                "Профиль пользователя ещё не создан"));

        profile.setBio(bio);
        profile.setAvatarUrl(avatarUrl);

        return profileRepository.save(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Profile> findByUserId(Long userId) {
        return userRepository.findById(userId)
                .flatMap(profileRepository::findByUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Profile getByUserIdOrThrow(Long userId) {
        return findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Профиль пользователя id=" + userId + " не найден"));
    }
}
