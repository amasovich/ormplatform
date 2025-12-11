package ru.mifi.ormplatform.service.impl;

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

    @Override
    public Module createModule(Long courseId,
                               String title,
                               Integer orderIndex,
                               String description) {

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Курс с id=" + courseId + " не найден"));

        // Нормализация строк
        String normalizedTitle = title.trim();
        String normalizedDescription = (description != null) ? description.trim() : null;

        // Сохраняем исходный индекс в НЕ изменяемую переменную
        final Integer originalIndex = orderIndex;

        // Все существующие модули
        List<Module> existingModules =
                moduleRepository.findAllByCourse_IdOrderByOrderIndexAsc(courseId);

        // Проверяем — занят ли originalIndex
        boolean exists = existingModules.stream()
                .anyMatch(m -> m.getOrderIndex().equals(originalIndex));

        // Вычисляем итоговый индекс (можем изменить эту переменную!)
        int finalIndex = originalIndex;

        if (exists) {
            int maxIndex = existingModules.stream()
                    .mapToInt(Module::getOrderIndex)
                    .max()
                    .orElse(0);
            finalIndex = maxIndex + 1;   // назначаем новый свободный
        }

        // Создание модуля
        Module module = new Module();
        module.setCourse(course);
        module.setTitle(normalizedTitle);
        module.setOrderIndex(finalIndex);
        module.setDescription(normalizedDescription);

        return moduleRepository.save(module);
    }

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

    @Override
    public Module updateModule(Long id,
                               String title,
                               Integer orderIndex,
                               String description) {

        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Модуль с id=" + id + " не найден"));

        if (title != null) {
            module.setTitle(title.trim());
        }

        if (description != null) {
            module.setDescription(description.trim());
        }

        if (orderIndex != null) {
            module.setOrderIndex(orderIndex);
        }

        return moduleRepository.save(module);
    }

    @Override
    public void deleteModule(Long id) {
        if (!moduleRepository.existsById(id)) {
            throw new IllegalArgumentException("Модуль не найден: id=" + id);
        }
        moduleRepository.deleteById(id);
    }

}
