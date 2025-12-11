package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.repository.TagRepository;
import ru.mifi.ormplatform.service.TagService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса тегов курсов.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Создание нового тега. Если такой уже есть — возвращаю существующий.
     */
    @Override
    public Tag createTag(String name) {

        // Нормализация строки
        String normalized = name.trim();

        // если тег уже существует — возвращаем его
        Optional<Tag> existing = tagRepository.findByName(normalized);
        if (existing.isPresent()) {
            return existing.get();
        }

        Tag tag = new Tag();
        tag.setName(normalized);

        return tagRepository.save(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

    /**
     * Поиск тегов по подстроке (регистронезависимо).
     */
    @Override
    @Transactional(readOnly = true)
    public List<Tag> searchByName(String namePart) {
        return tagRepository.findAllByNameContainingIgnoreCase(namePart);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    /**
     * Обновление тега.
     */
    @Override
    public Tag updateTag(Long id, String newName) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found: id=" + id));

        String normalized = newName.trim().toLowerCase();

        // Проверяем, что имя не занято другим тегом
        tagRepository.findByName(normalized)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalStateException("Tag with name '" + normalized + "' already exists");
                });

        tag.setName(normalized);
        return tagRepository.save(tag);
    }

    /**
     * Удаление тега.
     */
    @Override
    public void deleteTag(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new IllegalArgumentException("Tag not found: id=" + id);
        }
        tagRepository.deleteById(id);
    }

}

