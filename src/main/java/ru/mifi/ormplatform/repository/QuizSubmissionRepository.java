package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;

import java.util.List;

/**
 * Репозиторий для результатов прохождения квизов (quiz_submission).
 */
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {

    List<QuizSubmission> findAllByStudent_Id(Long studentId);

    List<QuizSubmission> findAllByQuiz_Id(Long quizId);
}
