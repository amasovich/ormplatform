package ru.mifi.ormplatform.service;

import ru.mifi.ormplatform.domain.entity.Assignment;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с практическими заданиями урока.
 * Содержит операции создания и чтения заданий.
 */
public interface AssignmentService {

    /**
     * Создаю новое практическое задание внутри урока.
     *
     * @param lessonId    идентификатор урока.
     * @param title       заголовок задания.
     * @param description подробное описание задания.
     * @param dueDate     дата дедлайна (может быть null).
     * @param maxScore    максимальная оценка за выполнение.
     * @return созданное и сохранённое задание.
     */
    Assignment createAssignment(Long lessonId,
                                String title,
                                String description,
                                LocalDate dueDate,
                                Integer maxScore);

    /**
     * Получаю задание по идентификатору.
     *
     * @param id идентификатор задания.
     * @return Optional с заданием.
     */
    Optional<Assignment> findById(Long id);

    /**
     * Получаю все задания конкретного урока.
     *
     * @param lessonId идентификатор урока.
     * @return список заданий.
     */
    List<Assignment> findByLesson(Long lessonId);
}
