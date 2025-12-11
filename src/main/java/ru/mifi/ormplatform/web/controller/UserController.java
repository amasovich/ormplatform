package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.service.UserService;
import ru.mifi.ormplatform.web.dto.UserCreateRequestDto;
import ru.mifi.ormplatform.web.dto.UserDto;
import ru.mifi.ormplatform.web.mapper.UserMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с пользователями:
 * регистрация, поиск, фильтрация по роли.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Создание нового пользователя.
     *
     * Пример:
     * POST /api/users
     * {
     *   "name": "Иван",
     *   "email": "ivan@example.com",
     *   "role": "STUDENT"
     * }
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserCreateRequestDto request) {

        User created = userService.createUser(
                request.getName(),
                request.getEmail(),
                request.getRole()
        );

        UserDto response = userMapper.toDto(created);

        return ResponseEntity.created(URI.create("/api/users/" + created.getId()))
                .body(response);
    }

    /**
     * Получение пользователя по id.
     *
     * GET /api/users/5
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        User user = userService.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + id));

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * Поиск пользователя по email.
     *
     * GET /api/users/search?email=xxx@yyy
     */
    @GetMapping("/search")
    public ResponseEntity<UserDto> getByEmail(@RequestParam String email) {

        User user = userService.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: email=" + email));

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    /**
     * Получение всех пользователей.
     *
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> result = userService.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Получение пользователей по роли.
     *
     * GET /api/users/role/STUDENT
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable UserRole role) {

        List<UserDto> result = userService.findByRole(role)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
