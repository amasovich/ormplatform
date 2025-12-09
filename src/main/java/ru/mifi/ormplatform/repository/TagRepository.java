package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для тегов (tags).
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String name);

    /**
     * Поиск тегов по подстроке в названии (для фильтрации на UI).
     */
    List<Tag> findAllByNameContainingIgnoreCase(String namePart);
}
