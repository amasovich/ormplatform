package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Submission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с решениями практических заданий.
 * Содержит операции отправки решения, оценивания, поиска и удаления.
 */
public interface SubmissionService {

    /**
     * Студент сдаёт решение задания.
     *
     * @param assignmentId идентификатор задания.
     * @param studentId    идентификатор студента (User с ролью STUDENT).
     * @param content      текст решения или ссылка на него.
     * @param submittedAt  время отправки (обычно LocalDateTime.now()).
     * @return созданная отправка.
     */
    Submission submitAssignment(Long assignmentId,
                                Long studentId,
                                String content,
                                LocalDateTime submittedAt);

    /**
     * Преподаватель выставляет оценку и комментарий.
     *
     * @param submissionId ID отправленной работы.
     * @param score        итоговая оценка.
     * @param feedback     комментарий преподавателя.
     * @return обновлённая отправка.
     */
    Submission gradeSubmission(Long submissionId,
                               Integer score,
                               String feedback);

    /**
     * Ищу отправку по идентификатору.
     *
     * @param id идентификатор.
     * @return Optional с отправкой, если найдена.
     */
    Optional<Submission> findById(Long id);

    /**
     * Получаю все отправки для конкретного задания.
     *
     * @param assignmentId ID задания.
     * @return список решений.
     */
    List<Submission> findByAssignment(Long assignmentId);

    /**
     * Получаю все отправки конкретного студента.
     *
     * @param studentId ID студента.
     * @return список отправок.
     */
    List<Submission> findByStudent(Long studentId);

    /**
     * Удаляю отправленное решение.
     *
     * @param id идентификатор отправки.
     */
    void deleteSubmission(Long id);

}
