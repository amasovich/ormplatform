package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.QuizSubmission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface QuizSubmissionService {

    QuizSubmission createSubmission(Long quizId,
                                    Long studentId,
                                    Integer score,
                                    LocalDateTime takenAt);

    QuizSubmission evaluateAndSaveSubmission(Long quizId,
                                             Long studentId,
                                             Map<Long, Long> answers,
                                             LocalDateTime takenAt);

    List<QuizSubmission> findByQuiz(Long quizId);

    List<QuizSubmission> findByStudent(Long studentId);
}

