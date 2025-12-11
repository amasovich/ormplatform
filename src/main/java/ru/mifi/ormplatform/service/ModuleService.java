package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Module;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с модулями курса.
 */
public interface ModuleService {

    /**
     * Создаю новый модуль внутри курса.
     *
     * @param courseId   идентификатор курса, к которому относится модуль.
     * @param title      человекочитаемое название модуля.
     * @param orderIndex порядковый номер модуля в курсе (1, 2, 3...).
     * @param description краткое описание содержимого модуля.
     * @return созданный и сохранённый модуль.
     */
    Module createModule(Long courseId,
                        String title,
                        Integer orderIndex,
                        String description);

    /**
     * Получаю модуль по идентификатору.
     *
     * @param id идентификатор модуля.
     * @return Optional с модулем, если он существует.
     */
    Optional<Module> findById(Long id);

    /**
     * Получаю все модули конкретного курса,
     * упорядоченные по полю orderIndex по возрастанию.
     *
     * @param courseId идентификатор курса.
     * @return список модулей.
     */
    List<Module> findByCourse(Long courseId);

    Module updateModule(Long id, String title, Integer orderIndex, String description);

    void deleteModule(Long id);

}

