package ru.mifi.ormplatform.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.service.TagService;
import ru.mifi.ormplatform.web.dto.TagDto;
import ru.mifi.ormplatform.web.mapper.TagMapper;

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
    public ResponseEntity<TagDto> create(@RequestBody TagDto dto) {
        Tag created = tagService.createTag(dto.getName());
        return ResponseEntity.ok(tagMapper.toDto(created));
    }

    /**
     * Обновить тег.
     *
     * PUT /api/tags/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagDto> update(
            @PathVariable Long id,
            @RequestBody TagDto dto
    ) {
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
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
