package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Lesson;

import java.util.List;

/**
 * Репозиторий для уроков (lesson).
 */
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    List<Lesson> findAllByModule_Id(Long moduleId);
}
