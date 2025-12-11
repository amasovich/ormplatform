package ru.mifi.ormplatform.web.mapper;

import org.springframework.stereotype.Component;
import ru.mifi.ormplatform.domain.entity.Tag;
import ru.mifi.ormplatform.web.dto.TagDto;

/**
 * Маппер тега курсов.
 */
@Component
public class TagMapper {

    public TagDto toDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
