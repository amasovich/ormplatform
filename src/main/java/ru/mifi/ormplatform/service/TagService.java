package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с тегами курсов.
 * Содержит операции создания, поиска, обновления и удаления тегов.
 */
public interface TagService {

    /**
     * Создаю новый тег.
     * Если тег с таким названием уже существует — возвращаю существующий.
     *
     * @param name название тега.
     * @return созданный или найденный тег.
     */
    Tag createTag(String name);

    /**
     * Ищу тег по названию.
     *
     * @param name название тега.
     * @return Optional с тегом, если найден.
     */
    Optional<Tag> findByName(String name);

    /**
     * Ищу теги по части названия (регистронезависимо).
     *
     * @param namePart часть названия.
     * @return список тегов.
     */
    List<Tag> searchByName(String namePart);

    /**
     * Получаю полный список тегов.
     *
     * @return список всех тегов.
     */
    List<Tag> findAll();

    /**
     * Обновляю название тега.
     *
     * @param id идентификатор тега.
     * @param newName новое название.
     * @return обновлённый тег.
     */
    Tag updateTag(Long id, String newName);

    /**
     * Удаляю тег по id.
     *
     * @param id идентификатор тега.
     */
    void deleteTag(Long id);

}
