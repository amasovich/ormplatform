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
 * <p>
 * Содержит CRUD-операции и бизнес-валидацию по уникальности имён категорий.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Создание новой категории.
     * Если категория с таким именем уже существует — возвращается существующая.
     */
    @Override
    public Category createCategory(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        String normalized = name.trim();

        Optional<Category> existing = categoryRepository.findByName(normalized);
        if (existing.isPresent()) {
            return existing.get();
        }

        Category category = new Category();
        category.setName(normalized);

        return categoryRepository.save(category);
    }

    /**
     * Обновление категории по id.
     */
    @Override
    public Category updateCategory(Long id, String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Название категории не может быть пустым");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена: " + id));

        category.setName(name.trim());
        return categoryRepository.save(category);
    }

    /**
     * Удаление категории по id.
     */
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Категория не найдена: " + id);
        }
        categoryRepository.deleteById(id);
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

    /**
     * Получение категории по id или исключение.
     */
    @Override
    @Transactional(readOnly = true)
    public Category getByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена: " + id));
    }
}
