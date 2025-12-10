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
 * Здесь инкапсулирую работу с UserRepository и добавляю простую бизнес-логику.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Явный конструктор для внедрения зависимостей.
     * Использую конструктор, чтобы не зависеть от Lombok.
     *
     * @param userRepository репозиторий пользователей.
     */
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String name, String email, UserRole role) {
        // На будущее сюда можно добавить проверку уникальности email и валидацию
        User user = new User();
        user.setName(name);
        user.setEmail(email);
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
        return userRepository.findByEmail(email);
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

