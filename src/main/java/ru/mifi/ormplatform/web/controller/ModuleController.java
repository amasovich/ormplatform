package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.service.ModuleService;
import ru.mifi.ormplatform.web.dto.ModuleCreateRequestDto;
import ru.mifi.ormplatform.web.dto.ModuleDto;
import ru.mifi.ormplatform.web.dto.ModuleUpdateRequestDto;
import ru.mifi.ormplatform.web.mapper.ModuleMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для управления модулями курсов.
 */
@RestController
@RequestMapping("/api")
public class ModuleController {

    private final ModuleService moduleService;
    private final ModuleMapper moduleMapper;

    public ModuleController(ModuleService moduleService,
                            ModuleMapper moduleMapper) {
        this.moduleService = moduleService;
        this.moduleMapper = moduleMapper;
    }

    /**
     * Создать модуль курса.
     * POST /api/courses/{courseId}/modules
     */
    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<ModuleDto> createModule(
            @PathVariable Long courseId,
            @Valid @RequestBody ModuleCreateRequestDto request) {

        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("Module title cannot be empty");
        }

        Module module = moduleService.createModule(
                courseId,
                request.getTitle(),
                request.getOrderIndex(),
                request.getDescription()
        );

        return ResponseEntity
                .created(URI.create("/api/modules/" + module.getId()))
                .body(moduleMapper.toDto(module));
    }

    /**
     * Получить один модуль.
     * GET /api/modules/{id}
     */
    @GetMapping("/modules/{id}")
    public ResponseEntity<ModuleDto> getModule(@PathVariable Long id) {

        Module module = moduleService.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Module not found: id=" + id));

        return ResponseEntity.ok(moduleMapper.toDto(module));
    }

    /**
     * Получить все модули курса.
     * GET /api/courses/{courseId}/modules
     */
    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleDto>> getModulesByCourse(@PathVariable Long courseId) {

        List<ModuleDto> list = moduleService.findByCourse(courseId)
                .stream()
                .map(moduleMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    /**
     * Обновить модуль.
     * PUT /api/modules/{id}
     */
    @PutMapping("/modules/{id}")
    public ResponseEntity<ModuleDto> updateModule(
            @PathVariable Long id,
            @Valid @RequestBody ModuleUpdateRequestDto request) {

        Module updated = moduleService.updateModule(
                id,
                request.getTitle(),
                request.getOrderIndex(),
                request.getDescription()
        );

        return ResponseEntity.ok(moduleMapper.toDto(updated));
    }

    /**
     * Удалить модуль.
     * DELETE /api/modules/{id}
     */
    @DeleteMapping("/modules/{id}")
    public ResponseEntity<Void> deleteModule(@PathVariable Long id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }
}
