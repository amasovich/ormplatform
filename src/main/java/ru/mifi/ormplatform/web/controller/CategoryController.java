package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.service.CategoryService;
import ru.mifi.ormplatform.web.dto.CategoryDto;
import ru.mifi.ormplatform.web.mapper.CategoryMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для управления категориями курсов.
 * <p>
 * Поддерживаются операции:
 * – получить все категории;
 * – создать категорию;
 * – обновить категорию;
 * – удалить категорию.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService,
                              CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    /**
     * Получить список всех категорий.
     *
     * GET /api/categories
     *
     * @return список всех категорий.
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll() {

        List<CategoryDto> list = categoryService.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    /**
     * Создать новую категорию.
     *
     * POST /api/categories
     *
     * @param dto DTO с названием категории.
     * @return созданная категория.
     */
    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestBody CategoryDto dto) {

        Category category = categoryService.createCategory(dto.getName());
        CategoryDto result = categoryMapper.toDto(category);

        // Возвращаем 201 Created
        return ResponseEntity
                .created(URI.create("/api/categories/" + result.getId()))
                .body(result);
    }

    /**
     * Обновить существующую категорию.
     *
     * PUT /api/categories/{id}
     *
     * @param id  идентификатор категории.
     * @param dto DTO с новым названием.
     * @return обновлённая категория.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Long id,
            @RequestBody CategoryDto dto) {

        Category updated = categoryService.updateCategory(id, dto.getName());
        return ResponseEntity.ok(categoryMapper.toDto(updated));
    }

    /**
     * Удалить категорию.
     *
     * DELETE /api/categories/{id}
     *
     * @param id идентификатор категории.
     * @return пустой ответ со статусом 204.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
