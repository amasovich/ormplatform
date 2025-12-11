package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.repository.CategoryRepository;
import ru.mifi.ormplatform.service.CategoryService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса категорий.
 * Содержит бизнес-валидацию, проверки уникальности и корректные исключения.
 */
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ============================================================================
    //                              CREATE CATEGORY
    // ============================================================================

    @Override
    public Category createCategory(String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        String normalized = name.trim();

        // Проверяем уникальность
        Optional<Category> existing = categoryRepository.findByName(normalized);
        if (existing.isPresent()) {
            return existing.get(); // Возвращаем существующую категорию
        }

        Category category = new Category();
        category.setName(normalized);

        return categoryRepository.save(category);
    }

    // ============================================================================
    //                              UPDATE CATEGORY
    // ============================================================================

    @Override
    public Category updateCategory(Long id, String name) {

        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Category name cannot be empty");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found: id=" + id));

        String normalized = name.trim();

        // Проверяем: не занято ли имя другой категорией
        categoryRepository.findByName(normalized)
                .filter(other -> !other.getId().equals(id))
                .ifPresent(other -> {
                    throw new ValidationException(
                            "Category with name '" + normalized + "' already exists"
                    );
                });

        category.setName(normalized);

        return categoryRepository.save(category);
    }

    // ============================================================================
    //                              DELETE CATEGORY
    // ============================================================================

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found: id=" + id);
        }
        categoryRepository.deleteById(id);
    }

    // ============================================================================
    //                              FIND METHODS
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name) {
        if (name == null || name.isBlank()) return Optional.empty();
        return categoryRepository.findByName(name.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Category not found: id=" + id));
    }
}
