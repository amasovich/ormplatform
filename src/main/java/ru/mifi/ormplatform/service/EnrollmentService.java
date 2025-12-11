package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления записями студентов на курсы.
 * Содержит операции добавления, обновления статуса,
 * удаления и выборки записей.
 */
public interface EnrollmentService {

    /**
     * Записываю студента на курс.
     *
     * @param courseId  идентификатор курса.
     * @param studentId идентификатор студента (роль STUDENT).
     * @return созданная запись Enrollment.
     */
    Enrollment enrollStudent(Long courseId, Long studentId);

    /**
     * Получаю записи конкретного студента.
     *
     * @param studentId идентификатор студента.
     * @return список записей.
     */
    List<Enrollment> findByStudent(Long studentId);

    /**
     * Получаю всех студентов, записанных на курс.
     *
     * @param courseId идентификатор курса.
     * @return список записей.
     */
    List<Enrollment> findByCourse(Long courseId);

    /**
     * Получаю запись по id.
     *
     * @param id идентификатор.
     * @return Optional с записью.
     */
    Optional<Enrollment> findById(Long id);

    /**
     * Обновляю статус существующей записи.
     *
     * @param id идентификатор записи.
     * @param status новый статус.
     * @return обновлённая запись.
     */
    Enrollment updateStatus(Long id, EnrollmentStatus status);

    /**
     * Удаляю запись студента с курса.
     *
     * @param id идентификатор записи.
     */
    void delete(Long id);
}
