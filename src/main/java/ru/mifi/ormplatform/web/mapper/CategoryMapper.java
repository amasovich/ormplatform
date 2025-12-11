package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Category;
import ru.mifi.ormplatform.web.dto.CategoryDto;

/**
 * Маппер для преобразования сущности {@link Category} в DTO {@link CategoryDto}.
 * <p>
 * Категории — простой справочник, поэтому маппер оставляет только безопасные поля.
 */
@Component
public class CategoryMapper {

    /**
     * Преобразует сущность {@link Category} в DTO.
     *
     * @param category JPA-сущность категории (может быть null)
     * @return DTO или null
     */
    public CategoryDto toDto(Category category) {
        if (category == null) return null;

        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }
}
