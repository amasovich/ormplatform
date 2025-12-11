package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с категориями курсов.
 * Содержит операции создания, обновления, удаления и поиска.
 */
public interface CategoryService {

    /**
     * Создаю новую категорию.
     * Если категория с таким именем уже существует, возвращается существующая.
     *
     * @param name название категории.
     * @return созданная или найденная категория.
     */
    Category createCategory(String name);

    /**
     * Обновляю существующую категорию.
     *
     * @param id   идентификатор категории.
     * @param name новое название категории.
     * @return обновлённая категория.
     */
    Category updateCategory(Long id, String name);

    /**
     * Удаляю категорию по идентификатору.
     *
     * @param id идентификатор категории.
     */
    void deleteCategory(Long id);

    /**
     * Ищу категорию по названию.
     *
     * @param name название категории.
     * @return Optional с найденной категорией.
     */
    Optional<Category> findByName(String name);

    /**
     * Получаю список всех категорий.
     *
     * @return все категории.
     */
    List<Category> findAll();

    /**
     * Получаю категорию по id или выбрасываю исключение.
     *
     * @param id идентификатор категории.
     * @return найденная категория.
     */
    Category getByIdOrThrow(Long id);
}
