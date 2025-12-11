package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с тегами курсов.
 */
public interface TagService {

    /**
     * Создаю новый тег.
     *
     * @param name название тега.
     * @return созданный тег.
     */
    Tag createTag(String name);

    /**
     * Нахожу тег по названию.
     *
     * @param name название.
     * @return тег, если найден.
     */
    Optional<Tag> findByName(String name);

    /**
     * Поиск тегов по подстроке (для фильтрации на UI).
     *
     * @param namePart часть названия.
     * @return список тегов.
     */
    List<Tag> searchByName(String namePart);

    /**
     * Получаю все теги.
     *
     * @return список тегов.
     */
    List<Tag> findAll();

    Tag updateTag(Long id, String newName);

    void deleteTag(Long id);

}

