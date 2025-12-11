package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.web.dto.CategoryDto;

/**
 * Маппер между Category и CategoryDto.
 */
@Component
public class CategoryMapper {

    /**
     * Преобразование сущности Category в DTO.
     */
    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    /**
     * Преобразование DTO в сущность Category.
     * Используется для создания и обновления.
     */
    public Category toEntity(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }
}
