package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.repository.CategoryRepository;
import ru.mifi.ormplatform.service.CategoryService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса категорий.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(String name) {

        // Нормализация строки
        String normalized = name.trim();

        // если такая категория уже существует — возвращаем её
        Optional<Category> existing = categoryRepository.findByName(normalized);
        if (existing.isPresent()) {
            return existing.get();
        }

        Category category = new Category();
        category.setName(normalized);

        return categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}

