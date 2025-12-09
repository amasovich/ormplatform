package ru.mifi.ormplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mifi.ormplatform.domain.entity.Enrollment;
import ru.mifi.ormplatform.domain.enums.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для записей на курсы (enrollment).
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Все записи студента.
     */
    List<Enrollment> findAllByStudent_Id(Long studentId);

    /**
     * Все записи на конкретный курс (например, для списка студентов).
     */
    List<Enrollment> findAllByCourse_Id(Long courseId);

    /**
     * Запись конкретного студента на конкретный курс.
     */
    Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);

    /**
     * Записи по статусу (active/completed/...).
     */
    List<Enrollment> findAllByStatus(EnrollmentStatus status);
}

