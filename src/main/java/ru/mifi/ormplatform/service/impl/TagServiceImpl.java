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

    @Override
    public Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return tagRepository.save(tag);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String name) {
        return tagRepository.findByName(name);
    }

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
}

