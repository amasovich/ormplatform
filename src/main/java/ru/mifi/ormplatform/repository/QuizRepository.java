package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Quiz;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для квизов (quiz).
 */
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    Optional<Quiz> findByModule_Id(Long moduleId);

    List<Quiz> findAllByCourse_Id(Long courseId);
}
