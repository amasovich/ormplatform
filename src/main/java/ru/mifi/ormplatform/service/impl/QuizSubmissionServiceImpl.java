package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.repository.QuizRepository;
import ru.mifi.ormplatform.repository.QuizSubmissionRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.QuizSubmissionService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Реализация сервиса для работы с результатами прохождения квизов.
 * На этом этапе я просто сохраняю факт прохождения и итоговый score.
 */
@Service
@Transactional
public class QuizSubmissionServiceImpl implements QuizSubmissionService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    /**
     * Конструктор с внедрением зависимостей.
     *
     * @param quizSubmissionRepository репозиторий результатов.
     * @param quizRepository           репозиторий квизов.
     * @param userRepository           репозиторий пользователей (студентов).
     */
    public QuizSubmissionServiceImpl(QuizSubmissionRepository quizSubmissionRepository,
                                     QuizRepository quizRepository,
                                     UserRepository userRepository) {
        this.quizSubmissionRepository = quizSubmissionRepository;
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    @Override
    public QuizSubmission createSubmission(Long quizId,
                                           Long studentId,
                                           Integer score,
                                           LocalDateTime takenAt) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Квиз с id=" + quizId + " не найден"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Студент с id=" + studentId + " не найден"));

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(takenAt);

        return quizSubmissionRepository.save(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizSubmission> findByQuiz(Long quizId) {
        return quizSubmissionRepository.findAllByQuiz_Id(quizId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizSubmission> findByStudent(Long studentId) {
        return quizSubmissionRepository.findAllByStudent_Id(studentId);
    }
}

