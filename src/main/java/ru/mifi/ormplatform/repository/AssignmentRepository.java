package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Assignment;

import java.util.List;

/**
 * Репозиторий для заданий (assignment).
 */
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findAllByLesson_Id(Long lessonId);
}
