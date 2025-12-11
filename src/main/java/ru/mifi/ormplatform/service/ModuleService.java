package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Module;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с модулями курса.
 * Содержит операции создания, поиска, обновления и удаления модулей.
 */
public interface ModuleService {

    /**
     * Создаю новый модуль внутри курса.
     *
     * @param courseId    идентификатор курса, к которому относится модуль.
     * @param title       название модуля.
     * @param orderIndex  желаемый порядковый индекс (если занят — система изменит автоматически).
     * @param description описание содержимого модуля.
     * @return созданный модуль.
     */
    Module createModule(Long courseId,
                        String title,
                        Integer orderIndex,
                        String description);

    /**
     * Получаю модуль по идентификатору.
     *
     * @param id идентификатор модуля.
     * @return Optional с найденным модулем.
     */
    Optional<Module> findById(Long id);

    /**
     * Получаю все модули курса, упорядоченные по orderIndex.
     *
     * @param courseId идентификатор курса.
     * @return список модулей.
     */
    List<Module> findByCourse(Long courseId);

    /**
     * Обновляю параметры существующего модуля.
     *
     * @param id          идентификатор модуля.
     * @param title       новое название.
     * @param orderIndex  новый порядок.
     * @param description новое описание.
     * @return обновлённый модуль.
     */
    Module updateModule(Long id,
                        String title,
                        Integer orderIndex,
                        String description);

    /**
     * Удаляю модуль.
     *
     * @param id идентификатор удаляемого модуля.
     */
    void deleteModule(Long id);
}
