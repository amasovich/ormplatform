package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.web.dto.TagDto;

/**
 * Маппер для преобразования сущности {@link Tag}
 * в DTO-модель {@link TagDto}.
 * <p>
 * Используется REST-контроллерами и сервисами
 * для передачи данных о тегах без участия JPA-сущностей.
 */
@Component
public class TagMapper {

    /**
     * Преобразует сущность {@link Tag} в {@link TagDto}.
     * <p>
     * Метод безопасен для null:
     * если входной объект равен null — возвращается null.
     *
     * @param tag сущность тега.
     * @return DTO-представление тега или null.
     */
    public TagDto toDto(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
