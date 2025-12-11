package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
 * Поддерживает:
 *  — ручное создание результата (createSubmission),
 *  — автоматическую оценку (evaluateAndSaveSubmission),
 *  — поиск результатов по студенту и квизу.
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

    // =========================================================================
    // CREATE (ручное создание результата)
    // =========================================================================

    @Override
    public QuizSubmission createSubmission(Long quizId,
                                           Long studentId,
                                           Integer score,
                                           LocalDateTime takenAt) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quiz not found: id=" + quizId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: id=" + studentId));

        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException(
                    "Only STUDENT can create a quiz submission");
        }

        // Проверка на повторную попытку (вариант №2)
        boolean alreadySubmitted = quizSubmissionRepository.findAllByStudent_Id(studentId)
                .stream()
                .anyMatch(s -> s.getQuiz().getId().equals(quizId));

        if (alreadySubmitted) {
            throw new ValidationException(
                    "Student has already submitted this quiz");
        }

        if (score != null && score < 0) {
            throw new ValidationException("Score cannot be negative");
        }

        QuizSubmission submission = new QuizSubmission();
        submission.setQuiz(quiz);
        submission.setStudent(student);
        submission.setScore(score);
        submission.setTakenAt(takenAt != null ? takenAt : LocalDateTime.now());

        return quizSubmissionRepository.save(submission);
    }

    // =========================================================================
    // FIND
    // =========================================================================

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

    // =========================================================================
    // EVALUATE AND SAVE (основная логика проверки квиза)
    // =========================================================================

    @Override
    public QuizSubmission evaluateAndSaveSubmission(Long quizId,
                                                    Long studentId,
                                                    Map<Long, Long> answers,
                                                    LocalDateTime takenAt) {

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Quiz not found: id=" + quizId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found: id=" + studentId));

        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException(
                    "Only STUDENT can submit a quiz");
        }

        // Проверка, что студент не проходил квиз ранее
        boolean alreadySubmitted = quizSubmissionRepository.findAllByStudent_Id(studentId)
                .stream()
                .anyMatch(s -> s.getQuiz().getId().equals(quizId));

        if (alreadySubmitted) {
            throw new ValidationException(
                    "Student has already completed this quiz");
        }

        if (answers == null || answers.isEmpty()) {
            throw new ValidationException(
                    "Answer map cannot be empty");
        }

        int score = 0;

        // Автоматический подсчёт баллов
        if (quiz.getQuestions() != null) {
            for (var question : quiz.getQuestions()) {

                Long selectedOptionId = answers.get(question.getId());
                if (selectedOptionId == null) {
                    continue;
                }

                boolean correct =
                        question.getOptions() != null &&
                                question.getOptions().stream()
                                        .anyMatch(o -> o.getId().equals(selectedOptionId) && o.isCorrect());

                if (correct) {
                    score++;
                }
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
