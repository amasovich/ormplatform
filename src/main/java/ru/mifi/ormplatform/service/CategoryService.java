package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Category;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с категориями курсов.
 */
public interface CategoryService {

    /**
     * Создаю новую категорию.
     *
     * @param name название категории.
     * @return созданная категория.
     */
    Category createCategory(String name);

    /**
     * Нахожу категорию по названию.
     *
     * @param name название.
     * @return категория, если найдена.
     */
    Optional<Category> findByName(String name);

    /**
     * Получаю список всех категорий.
     *
     * @return список категорий.
     */
    List<Category> findAll();
}

