package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Category;

import java.util.Optional;

/**
 * Репозиторий для категорий курсов (categories).
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Нахожу категорию по названию.
     *
     * @param name название категории.
     * @return категория, если найдена.
     */
    Optional<Category> findByName(String name);
}

