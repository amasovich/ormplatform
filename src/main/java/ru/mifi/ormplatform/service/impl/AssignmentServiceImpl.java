package ru.mifi.ormplatform.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Assignment;
import ru.mifi.ormplatform.domain.entity.Lesson;
import ru.mifi.ormplatform.repository.AssignmentRepository;
import ru.mifi.ormplatform.repository.LessonRepository;
import ru.mifi.ormplatform.service.AssignmentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса заданий.
 */
@Service
@Transactional
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final LessonRepository lessonRepository;

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository,
                                 LessonRepository lessonRepository) {
        this.assignmentRepository = assignmentRepository;
        this.lessonRepository = lessonRepository;
    }

    @Override
    public Assignment createAssignment(Long lessonId,
                                       String title,
                                       String description,
                                       LocalDate dueDate,
                                       Integer maxScore) {

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Урок с id=" + lessonId + " не найден"));

        // Нормализация строк
        String normalizedTitle = title.trim();
        String normalizedDescription = description != null ? description.trim() : null;

        // Валидация входных данных
        if (normalizedTitle.isEmpty()) {
            throw new IllegalArgumentException("Название задания не может быть пустым");
        }
        if (normalizedDescription == null || normalizedDescription.isEmpty()) {
            throw new IllegalArgumentException("Описание задания не может быть пустым");
        }
        if (maxScore == null || maxScore <= 0) {
            throw new IllegalArgumentException("maxScore должен быть положительным числом");
        }

        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(normalizedTitle);
        assignment.setDescription(normalizedDescription);
        assignment.setDueDate(dueDate);
        assignment.setMaxScore(maxScore);

        return assignmentRepository.save(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Assignment> findById(Long id) {
        return assignmentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> findByLesson(Long lessonId) {
        return assignmentRepository.findAllByLesson_Id(lessonId);
    }
}

