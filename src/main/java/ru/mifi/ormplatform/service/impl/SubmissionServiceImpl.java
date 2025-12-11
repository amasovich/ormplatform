package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.domain.entity.User;
import ru.mifi.ormplatform.domain.enums.UserRole;
import ru.mifi.ormplatform.repository.AssignmentRepository;
import ru.mifi.ormplatform.repository.SubmissionRepository;
import ru.mifi.ormplatform.repository.UserRepository;
import ru.mifi.ormplatform.service.SubmissionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса решений заданий.
 */
@Service
@Transactional
public class SubmissionServiceImpl implements SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public SubmissionServiceImpl(SubmissionRepository submissionRepository,
                                 AssignmentRepository assignmentRepository,
                                 UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Submission submitAssignment(Long assignmentId,
                                       Long studentId,
                                       String content,
                                       LocalDateTime submittedAt) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Задание с id=" + assignmentId + " не найдено"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Пользователь с id=" + studentId + " не найден"));

        // 🔥 Проверка роли
        if (student.getRole() != UserRole.STUDENT) {
            throw new IllegalArgumentException(
                    "Только STUDENT может сдавать задание");
        }

        // 🔥 Проверка на повторную сдачу
        Optional<Submission> existing =
                submissionRepository.findByAssignment_IdAndStudent_Id(assignmentId, studentId);

        if (existing.isPresent()) {
            throw new IllegalStateException(
                    "Студент уже сдавал это задание");
        }

        // Нормализация текста решения
        String normalizedContent = (content != null) ? content.trim() : "";
        if (normalizedContent.isEmpty()) {
            throw new IllegalArgumentException("Содержимое решения не может быть пустым");
        }

        // Защита от null submittedAt
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setContent(normalizedContent);
        submission.setSubmittedAt(submittedAt);

        submission.setScore(null);
        submission.setFeedback(null);

        return submissionRepository.save(submission);
    }

    @Override
    public Submission gradeSubmission(Long submissionId,
                                      Integer score,
                                      String feedback) {

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Отправка с id=" + submissionId + " не найдена"));

        Assignment assignment = submission.getAssignment();
        Integer maxScore = assignment.getMaxScore();

        // Проверка валидности оценки
        if (score == null || score < 0 || score > maxScore) {
            throw new IllegalArgumentException(
                    "Оценка должна быть от 0 до " + maxScore);
        }

        // Нормализация feedback (если есть)
        String normalizedFeedback =
                (feedback != null) ? feedback.trim() : null;

        submission.setScore(score);
        submission.setFeedback(normalizedFeedback);

        return submissionRepository.save(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Submission> findById(Long id) {
        return submissionRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Submission> findByAssignment(Long assignmentId) {
        return submissionRepository.findAllByAssignment_Id(assignmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Submission> findByStudent(Long studentId) {
        return submissionRepository.findAllByStudent_Id(studentId);
    }
}