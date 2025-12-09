package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Profile;
import ru.mifi.ormplatform.domain.entity.User;

import java.util.Optional;

/**
 * Репозиторий для профилей (profiles).
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /**
     * Нахожу профиль по пользователю.
     *
     * @param user пользователь.
     * @return профиль, если он есть.
     */
    Optional<Profile> findByUser(User user);
}

