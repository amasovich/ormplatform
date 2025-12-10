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
 * Здесь я инкапсулирую работу с репозиториями Module и Course.
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

        Module module = new Module();
        module.setCourse(course);
        module.setTitle(title);
        module.setOrderIndex(orderIndex);
        module.setDescription(description);

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
}
