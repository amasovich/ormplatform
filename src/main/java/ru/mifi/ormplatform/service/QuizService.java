package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Quiz;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с квизами (тестами) по модулям курса.
 */
public interface QuizService {

    /**
     * Создаю квиз для конкретного модуля и курса.
     */
    Quiz createQuiz(Long courseId,
                    Long moduleId,
                    String title,
                    Integer timeLimitMinutes);

    Optional<Quiz> findById(Long id);

    Optional<Quiz> findByModule(Long moduleId);

    List<Quiz> findByCourse(Long courseId);

    Quiz updateQuiz(Long id, String title, Integer timeLimitMinutes);

    void deleteQuiz(Long id);

}