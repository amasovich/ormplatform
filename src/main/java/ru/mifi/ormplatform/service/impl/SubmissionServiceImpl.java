package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Submission;
import ru.mifi.ormplatform.domain.entity.User;
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
                        "Студент с id=" + studentId + " не найден"));

        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setStudent(student);
        submission.setContent(content);
        submission.setSubmittedAt(submittedAt);

        // Оценка и feedback по умолчанию пустые
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

        submission.setScore(score);
        submission.setFeedback(feedback);

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

