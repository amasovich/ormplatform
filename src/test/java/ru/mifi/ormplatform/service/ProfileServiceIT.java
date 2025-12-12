package ru.mifi.ormplatform.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ProfileServiceIT {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Profile User");
        user.setEmail("profile@test.com");
        user.setRole(UserRole.STUDENT);
        user = userRepository.save(user);
    }

    @Test
    void profile_is_created_automatically_on_update() {
        Profile profile = profileService.updateProfile(
                user.getId(),
                "My bio",
                "avatar.png"
        );

        assertThat(profile).isNotNull();
        assertThat(profile.getBio()).isEqualTo("My bio");
        assertThat(profile.getAvatarUrl()).isEqualTo("avatar.png");
    }

    @Test
    void can_get_profile_by_user_id() {
        profileService.updateProfile(user.getId(), "Bio", null);

        Profile profile = profileService.getByUserId(user.getId());

        assertThat(profile.getUser().getId()).isEqualTo(user.getId());
    }

    @Test
    void get_profile_for_unknown_user_throws_exception() {
        assertThrows(EntityNotFoundException.class, () ->
                profileService.getByUserId(999L)
        );
    }
}
