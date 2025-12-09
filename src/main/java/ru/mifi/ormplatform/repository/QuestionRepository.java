package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Question;

import java.util.List;

/**
 * Репозиторий для вопросов квизов (question).
 */
public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByQuiz_Id(Long quizId);
}
