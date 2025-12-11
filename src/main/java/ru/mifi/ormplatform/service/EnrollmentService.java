package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления записями студентов на курсы.
 */
public interface EnrollmentService {

    /**
     * Записываю студента на курс.
     *
     * @param courseId  идентификатор курса.
     * @param studentId идентификатор студента (User с ролью STUDENT).
     * @return созданная запись Enrollment.
     */
    Enrollment enrollStudent(Long courseId, Long studentId);

    /**
     * Возвращаю все записи конкретного студента.
     *
     * @param studentId идентификатор студента.
     * @return список Enrollment.
     */
    List<Enrollment> findByStudent(Long studentId);

    /**
     * Возвращаю всех студентов, записанных на курс.
     *
     * @param courseId идентификатор курса.
     * @return список Enrollment.
     */
    List<Enrollment> findByCourse(Long courseId);

    /**
     * Ищу запись по идентификатору.
     *
     * @param id идентификатор записи.
     * @return Optional с записью, если найдена.
     */
    Optional<Enrollment> findById(Long id);

    /**
     * Обновляю статус записи.
     *
     * @param id идентификатор записи.
     * @param status новый статус.
     * @return обновлённая запись.
     */
    Enrollment updateStatus(Long id, EnrollmentStatus status);

    /**
     * Удаляю запись студента на курс (отписываю).
     *
     * @param id идентификатор записи.
     */
    void delete(Long id);

}
