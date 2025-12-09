package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Module;

import java.util.List;

/**
 * Репозиторий для модулей (module).
 */
public interface ModuleRepository extends JpaRepository<Module, Long> {

    /**
     * Модули по курсу, упорядоченные по orderIndex.
     */
    List<Module> findAllByCourse_IdOrderByOrderIndexAsc(Long courseId);
}
