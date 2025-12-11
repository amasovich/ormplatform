package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.repository.TagRepository;
import ru.mifi.ormplatform.service.TagService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса для работы с тегами.
 * Содержит валидацию входных данных, нормализацию строк,
 * проверки уникальности и корректную обработку ошибок.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // ============================================================
    //                         CREATE TAG
    // ============================================================

    @Override
    public Tag createTag(String name) {

        // Валидация
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Tag name cannot be empty");
        }

        // Нормализация
        String normalized = name.trim().toLowerCase();

        // Проверка существования
        Optional<Tag> existing = tagRepository.findByName(normalized);
        if (existing.isPresent()) {
            return existing.get();
        }

        // Создание нового тега
        Tag tag = new Tag();
        tag.setName(normalized);

        return tagRepository.save(tag);
    }

    // ============================================================
    //                         READ TAGS
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        if (name == null || name.isBlank()) return Optional.empty();
        return tagRepository.findByName(name.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> searchByName(String namePart) {
        if (namePart == null || namePart.isBlank()) {
            return List.of();
        }
        return tagRepository.findAllByNameContainingIgnoreCase(namePart.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    // ============================================================
    //                         UPDATE TAG
    // ============================================================

    @Override
    public Tag updateTag(Long id, String newName) {

        if (newName == null || newName.trim().isEmpty()) {
            throw new ValidationException("New tag name cannot be empty");
        }

        String normalized = newName.trim().toLowerCase();

        // Получение тега или ошибка 404
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Tag not found: id=" + id));

        // Проверка уникальности: имя занято другим тегом
        tagRepository.findByName(normalized)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ValidationException(
                            "Tag with name '" + normalized + "' already exists"
                    );
                });

        tag.setName(normalized);
        return tagRepository.save(tag);
    }

    // ============================================================
    //                         DELETE TAG
    // ============================================================

    @Override
    public void deleteTag(Long id) {

        if (!tagRepository.existsById(id)) {
            throw new EntityNotFoundException("Tag not found: id=" + id);
        }

        tagRepository.deleteById(id);
    }
}
