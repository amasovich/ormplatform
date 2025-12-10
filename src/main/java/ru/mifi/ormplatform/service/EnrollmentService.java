package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Enrollment;

import java.util.List;

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
}
