package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Submission;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для решений заданий (submission).
 */
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    /**
     * Все решения одного студента.
     */
    List<Submission> findAllByStudent_Id(Long studentId);

    /**
     * Все решения по заданию.
     */
    List<Submission> findAllByAssignment_Id(Long assignmentId);

    /**
     * Решение конкретного студента по конкретному заданию.
     */
    Optional<Submission> findByAssignment_IdAndStudent_Id(Long assignmentId, Long studentId);
}
