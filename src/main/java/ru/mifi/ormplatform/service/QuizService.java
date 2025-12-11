package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Quiz;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с квизами (тестами), привязанными к модулям курса.
 */
public interface QuizService {

    /**
     * Создаю квиз для конкретного модуля указанного курса.
     *
     * @param courseId идентификатор курса.
     * @param moduleId идентификатор модуля.
     * @param title название квиза.
     * @param timeLimitMinutes ограничение по времени (минуты), может быть null.
     * @return созданный квиз.
     */
    Quiz createQuiz(Long courseId,
                    Long moduleId,
                    String title,
                    Integer timeLimitMinutes);

    /**
     * Ищу квиз по идентификатору.
     */
    Optional<Quiz> findById(Long id);

    /**
     * Ищу квиз по идентификатору модуля.
     */
    Optional<Quiz> findByModule(Long moduleId);

    /**
     * Возвращаю квизы всех модулей курса.
     */
    List<Quiz> findByCourse(Long courseId);

    /**
     * Обновляю квиз.
     *
     * @param id идентификатор квиза.
     * @param title новое название.
     * @param timeLimitMinutes новое ограничение по времени.
     */
    Quiz updateQuiz(Long id, String title, Integer timeLimitMinutes);

    /**
     * Удаляю квиз.
     */
    void deleteQuiz(Long id);
}
