package ru.mifi.ormplatform.service.impl;

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
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email, UserRole role) {

        // Проверка входных данных
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Некорректный email: " + email);
        }
        if (role == null) {
            throw new IllegalArgumentException("Роль пользователя не может быть null");
        }

        // Нормализация данных
        String normalizedName = name.trim();
        String normalizedEmail = email.trim().toLowerCase();

        // Проверка уникальности email
        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalStateException(
                    "Пользователь с email=" + normalizedEmail + " уже существует");
        }

        // Создание пользователя
        User user = new User();
        user.setName(normalizedName);
        user.setEmail(normalizedEmail);
        user.setRole(role);

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
