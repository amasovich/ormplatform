package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса пользователей.
 * Содержит валидацию, проверки уникальности и стандартную обработку ошибок.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ===========================================================
    //                        CREATE USER
    // ===========================================================

    @Override
    public User createUser(String name, String email, UserRole role) {

        // -----------------------------
        // ВАЛИДАЦИЯ ВХОДНЫХ ДАННЫХ
        // -----------------------------
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("User name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email cannot be empty");
        }
        if (!email.contains("@")) {
            throw new ValidationException("Invalid email format: " + email);
        }
        if (role == null) {
            throw new ValidationException("User role cannot be null");
        }

        // Нормализация
        String normalizedName = name.trim();
        String normalizedEmail = email.trim().toLowerCase();

        // -----------------------------
        // ПРОВЕРКА УНИКАЛЬНОСТИ EMAIL
        // -----------------------------
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new ValidationException(
                    "User with email '" + normalizedEmail + "' already exists"
            );
        }

        // -----------------------------
        // СОЗДАНИЕ ПОЛЬЗОВАТЕЛЯ
        // -----------------------------
        User user = new User();
        user.setName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setRole(role);

        return userRepository.save(user);
    }

    // ===========================================================
    //                        FIND USER
    // ===========================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        if (role == null) {
            throw new ValidationException("Role cannot be null");
        }
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    // ===========================================================
    //         ВСПОМОГАТЕЛЬНЫЙ МЕТОД ДЛЯ КОНТРОЛЛЕРОВ
    // ===========================================================

    /**
     * Унифицированный метод для ситуаций, когда пользователь должен существовать.
     */
    public User getByIdOrThrow(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found: id=" + id)
        );
    }
}
