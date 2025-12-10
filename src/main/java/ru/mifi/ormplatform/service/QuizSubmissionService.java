package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.QuizSubmission;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с результатами прохождения квизов.
 * На этом шаге я просто сохраняю факт прохождения и итоговый score.
 * Логику проверки ответов добавлю позже.
 */
public interface QuizSubmissionService {

    QuizSubmission createSubmission(Long quizId,
                                    Long studentId,
                                    Integer score,
                                    LocalDateTime takenAt);

    List<QuizSubmission> findByQuiz(Long quizId);

    List<QuizSubmission> findByStudent(Long studentId);
}
