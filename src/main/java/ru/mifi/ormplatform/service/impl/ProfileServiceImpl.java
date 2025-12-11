package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.repository.ProfileRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.ProfileService;

/**
 * Реализация ProfileService.
 * Включает корректную обработку ошибок, нормализацию входных данных
 * и автоматическое создание профиля при обновлении.
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

    // ============================================================================
    //                             GET PROFILE
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Profile getByUserId(Long userId) {

        if (userId == null) {
            throw new ValidationException("userId cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + userId));

        return profileRepository.findByUser(user)
                .orElseThrow(() ->
                        new EntityNotFoundException("Profile not found for user id=" + userId));
    }

    // ============================================================================
    //                           UPDATE OR CREATE PROFILE
    // ============================================================================

    @Override
    public Profile updateProfile(Long userId,
                                 String bio,
                                 String avatarUrl) {

        if (userId == null) {
            throw new ValidationException("userId cannot be null");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + userId));

        // Ищем профиль или создаём новый
        Profile profile = profileRepository.findByUser(user)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUser(user);
                    return p;
                });

        // -------- Нормализация входных данных --------
        String normalizedBio = (bio != null && !bio.isBlank()) ? bio.trim() : null;
        String normalizedAvatar = (avatarUrl != null && !avatarUrl.isBlank()) ? avatarUrl.trim() : null;

        profile.setBio(normalizedBio);
        profile.setAvatarUrl(normalizedAvatar);

        return profileRepository.save(profile);
    }
}
