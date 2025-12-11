package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Submission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с решениями практических заданий.
 */
public interface SubmissionService {

    /**
     * Студент сдаёт задание.
     *
     * @param assignmentId идентификатор задания.
     * @param studentId    идентификатор студента (User с ролью STUDENT).
     * @param content      текст решения или ссылка на него.
     * @param submittedAt  дата/время отправки (обычно LocalDateTime.now()).
     * @return созданная отправка.
     */
    Submission submitAssignment(Long assignmentId,
                                Long studentId,
                                String content,
                                LocalDateTime submittedAt);

    /**
     * Преподаватель выставляет оценку и комментарий к уже существующей отправке.
     */
    Submission gradeSubmission(Long submissionId,
                               Integer score,
                               String feedback);

    Optional<Submission> findById(Long id);

    List<Submission> findByAssignment(Long assignmentId);

    List<Submission> findByStudent(Long studentId);

    void deleteSubmission(Long id);

}

