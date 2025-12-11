package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
 * Реализация сервиса решений практических заданий.
 * Содержит валидацию, проверки ролей,
 * защиту от дубликатов и корректную обработку ошибок.
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

    // =========================================================================
    //                           SUBMIT ASSIGNMENT
    // =========================================================================

    @Override
    public Submission submitAssignment(Long assignmentId,
                                       Long studentId,
                                       String content,
                                       LocalDateTime submittedAt) {

        // -----------------------------
        // Валидация входных данных
        // -----------------------------
        if (studentId == null) {
            throw new ValidationException("studentId is required");
        }
        if (assignmentId == null) {
            throw new ValidationException("assignmentId is required");
        }
        if (content == null || content.trim().isEmpty()) {
            throw new ValidationException("Submission content cannot be empty");
        }

        String normalizedContent = content.trim();
        LocalDateTime timestamp = (submittedAt != null) ? submittedAt : LocalDateTime.now();

        // -----------------------------
        // Получение задания и студента
        // -----------------------------
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Assignment not found: id=" + assignmentId));

        User student = userRepository.findById(studentId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found: id=" + studentId));

        // -----------------------------
        // Проверка роли
        // -----------------------------
        if (student.getRole() != UserRole.STUDENT) {
            throw new ValidationException("Only a STUDENT may submit an assignment");
        }

        // -----------------------------
        // Проверка на повторную сдачу
        // -----------------------------
        submissionRepository.findByAssignment_IdAndStudent_Id(assignmentId, studentId)
                .ifPresent(existing -> {
                    throw new ValidationException("Student has already submitted this assignment");
                });

        // -----------------------------
        // Создание новой отправки
        // -----------------------------
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setContent(normalizedContent);
        submission.setSubmittedAt(timestamp);

        submission.setScore(null);
        submission.setFeedback(null);

        return submissionRepository.save(submission);
    }

    // =========================================================================
    //                             GRADE SUBMISSION
    // =========================================================================

    @Override
    public Submission gradeSubmission(Long submissionId,
                                      Integer score,
                                      String feedback) {

        // -----------------------------
        // Валидация входных данных
        // -----------------------------
        if (score == null) {
            throw new ValidationException("Score is required");
        }

        // -----------------------------
        // Получение отправки
        // -----------------------------
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Submission not found: id=" + submissionId));

        Assignment assignment = submission.getAssignment();
        Integer maxScore = assignment.getMaxScore();

        // -----------------------------
        // Проверка диапазона оценки
        // -----------------------------
        if (score < 0 || score > maxScore) {
            throw new ValidationException(
                    "Score must be between 0 and " + maxScore
            );
        }

        // -----------------------------
        // Нормализация комментария
        // -----------------------------
        String normalizedFeedback =
                (feedback != null && !feedback.isBlank()) ? feedback.trim() : null;

        // -----------------------------
        // Обновление сущности
        // -----------------------------
        submission.setScore(score);
        submission.setFeedback(normalizedFeedback);

        return submissionRepository.save(submission);
    }

    // =========================================================================
    //                                FIND
    // =========================================================================

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

    // =========================================================================
    //                                DELETE
    // =========================================================================

    @Override
    public void deleteSubmission(Long id) {

        if (!submissionRepository.existsById(id)) {
            throw new EntityNotFoundException("Submission not found: id=" + id);
        }

        submissionRepository.deleteById(id);
    }
}
