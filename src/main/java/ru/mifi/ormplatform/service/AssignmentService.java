package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Assignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с практическими заданиями урока.
 */
public interface AssignmentService {

    /**
     * Создаю задание для конкретного урока.
     */
    Assignment createAssignment(Long lessonId,
                                String title,
                                String description,
                                LocalDate dueDate,
                                Integer maxScore);

    Optional<Assignment> findById(Long id);

    /**
     * Возвращаю все задания для указанного урока.
     *
     * @param lessonId идентификатор урока.
     * @return список заданий.
     */
    List<Assignment> findByLesson(Long lessonId);
}

