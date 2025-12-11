package ru.mifi.ormplatform.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.service.TagService;
import ru.mifi.ormplatform.web.dto.TagDto;
import ru.mifi.ormplatform.web.mapper.TagMapper;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST-контроллер для работы с тегами курсов.
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    public TagController(TagService tagService,
                         TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    /**
     * Получить список всех тегов.
     *
     * GET /api/tags
     */
    @GetMapping
    public ResponseEntity<List<TagDto>> getAll() {
        List<TagDto> list = tagService.findAll()
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }

    /**
     * Поиск тегов по подстроке (регистронезависимо).
     *
     * GET /api/tags/search?query=java
     */
    @GetMapping("/search")
    public ResponseEntity<List<TagDto>> search(@RequestParam("query") String query) {
        List<TagDto> result = tagService.searchByName(query)
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    /**
     * Создать новый тег.
     *
     * POST /api/tags
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody TagDto dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }

        Tag created = tagService.createTag(dto.getName());
        TagDto response = tagMapper.toDto(created);

        return ResponseEntity
                .created(URI.create("/api/tags/" + created.getId()))
                .body(response);
    }

    /**
     * Обновить тег.
     *
     * PUT /api/tags/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TagDto dto
    ) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be empty");
        }

        Tag updated = tagService.updateTag(id, dto.getName());
        return ResponseEntity.ok(tagMapper.toDto(updated));
    }

    /**
     * Удалить тег.
     *
     * DELETE /api/tags/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        // Если тега нет — TagService должен выбросить EntityNotFoundException
        tagService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }
}
