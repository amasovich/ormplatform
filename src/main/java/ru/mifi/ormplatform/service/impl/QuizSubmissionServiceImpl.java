package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.domain.entity.QuizSubmission;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.QuizRepository;
import ru.mifi.ormplatform.repository.QuizSubmissionRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.QuizSubmissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Реализация сервиса для работы с результатами прохождения квизов.
 */
@Service
@Transactional
public class QuizSubmissionServiceImpl implements QuizSubmissionService {

    private final QuizSubmissionRepository quizSubmissionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

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
                        "Пользователь с id=" + studentId + " не найден"));

        // Проверка роли
        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalStateException("Только STUDENT может отправлять прохождение квиза");
        }

        // Запрет повторного прохождения
        List<QuizSubmission> existing = quizSubmissionRepository.findAllByStudent_Id(studentId)
                .stream()
                .filter(s -> s.getQuiz().getId().equals(quizId))
                .toList();

        if (!existing.isEmpty()) {
            throw new IllegalStateException("Этот студент уже проходил указанный квиз");
        }

        // Валидация оценки
        if (score != null && score < 0) {
            throw new IllegalArgumentException("Score не может быть отрицательным");
        }

        // Если не передано время — ставим текущее
        LocalDateTime timestamp = (takenAt != null ? takenAt : LocalDateTime.now());

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(timestamp);

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

    @Override
    public QuizSubmission evaluateAndSaveSubmission(Long quizId,
                                                    Long studentId,
                                                    Map<Long, Long> answers,
                                                    LocalDateTime takenAt) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Квиз не найден: " + quizId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: " + studentId));

        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalStateException("Только студент может проходить квиз");
        }

        // Запрет повторного прохождения
        boolean alreadyPassed = quizSubmissionRepository.findAllByStudent_Id(studentId)
                .stream()
                .anyMatch(s -> s.getQuiz().getId().equals(quizId));

        if (alreadyPassed) {
            throw new IllegalStateException("Студент уже проходил этот квиз");
        }

        int score = 0;

        if (quiz.getQuestions() != null) {
            for (var question : quiz.getQuestions()) {

                Long selectedOptionId = answers.get(question.getId());
                if (selectedOptionId == null) continue;

                if (question.getOptions() == null) continue;

                boolean correct = question.getOptions()
                        .stream()
                        .anyMatch(o -> o.getId().equals(selectedOptionId) && o.isCorrect());

                if (correct) score++;
            }
        }

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(takenAt != null ? takenAt : LocalDateTime.now());

        return quizSubmissionRepository.save(submission);
    }

}
