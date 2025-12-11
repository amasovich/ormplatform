package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.ModuleRepository;
import ru.mifi.ormplatform.service.ModuleService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса модулей курса.
 * Содержит валидацию входных данных, нормализацию строк,
 * корректную обработку ошибок и управление логикой orderIndex.
 */
@Service
@Transactional
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    public ModuleServiceImpl(ModuleRepository moduleRepository,
                             CourseRepository courseRepository) {
        this.moduleRepository = moduleRepository;
        this.courseRepository = courseRepository;
    }

    // ============================================================================
    //                                 CREATE MODULE
    // ============================================================================

    @Override
    public Module createModule(Long courseId,
                               String title,
                               Integer orderIndex,
                               String description) {

        // -----------------------------
        // Валидация входных данных
        // -----------------------------
        if (courseId == null) {
            throw new ValidationException("courseId is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Module title cannot be empty");
        }
        if (orderIndex == null || orderIndex < 1) {
            throw new ValidationException("orderIndex must be >= 1");
        }

        // -----------------------------
        // Получение курса
        // -----------------------------
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + courseId));

        // -----------------------------
        // Нормализация строковых данных
        // -----------------------------
        String normalizedTitle = title.trim();
        String normalizedDescription =
                (description != null && !description.isBlank())
                        ? description.trim()
                        : null;

        // -----------------------------
        // Проверка существующего orderIndex
        // -----------------------------
        List<Module> existing =
                moduleRepository.findAllByCourse_IdOrderByOrderIndexAsc(courseId);

        boolean indexTaken = existing.stream()
                .anyMatch(m -> m.getOrderIndex().equals(orderIndex));

        int finalIndex = orderIndex;

        if (indexTaken) {
            int maxIndex = existing.stream()
                    .mapToInt(Module::getOrderIndex)
                    .max()
                    .orElse(0);

            finalIndex = maxIndex + 1;
        }

        // -----------------------------
        // Создание модуля
        // -----------------------------
        Module module = new Module();
        module.setCourse(course);
        module.setTitle(normalizedTitle);
        module.setDescription(normalizedDescription);
        module.setOrderIndex(finalIndex);

        return moduleRepository.save(module);
    }

    // ============================================================================
    //                                  FIND
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Module> findById(Long id) {
        return moduleRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Module> findByCourse(Long courseId) {
        return moduleRepository.findAllByCourse_IdOrderByOrderIndexAsc(courseId);
    }

    // ============================================================================
    //                                 UPDATE MODULE
    // ============================================================================

    @Override
    public Module updateModule(Long id,
                               String title,
                               Integer orderIndex,
                               String description) {

        // Получение модуля
        Module module = moduleRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Module not found: id=" + id));

        // -----------------------------
        // Обновление title
        // -----------------------------
        if (title != null) {
            if (title.trim().isEmpty()) {
                throw new ValidationException("Module title cannot be empty");
            }
            module.setTitle(title.trim());
        }

        // -----------------------------
        // Обновление description
        // -----------------------------
        if (description != null) {
            module.setDescription(description.trim().isEmpty() ? null : description.trim());
        }

        // -----------------------------
        // Обновление orderIndex
        // -----------------------------
        if (orderIndex != null) {
            if (orderIndex < 1) {
                throw new ValidationException("orderIndex must be >= 1");
            }
            module.setOrderIndex(orderIndex);
        }

        return moduleRepository.save(module);
    }

    // ============================================================================
    //                                 DELETE MODULE
    // ============================================================================

    @Override
    public void deleteModule(Long id) {

        if (!moduleRepository.existsById(id)) {
            throw new EntityNotFoundException("Module not found: id=" + id);
        }

        moduleRepository.deleteById(id);
    }
}
