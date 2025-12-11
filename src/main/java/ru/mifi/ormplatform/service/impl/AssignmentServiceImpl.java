package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
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
 * Реализация AssignmentService.
 * Включает строгую валидацию входных данных, нормализацию строк и корректную
 * обработку ошибок (EntityNotFoundException, ValidationException).
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

    // ============================================================================
    //                                CREATE ASSIGNMENT
    // ============================================================================

    @Override
    public Assignment createAssignment(Long lessonId,
                                       String title,
                                       String description,
                                       LocalDate dueDate,
                                       Integer maxScore) {

        // -----------------------------
        // Валидация входных параметров
        // -----------------------------
        if (lessonId == null) {
            throw new ValidationException("lessonId is required");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("Assignment title cannot be empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new ValidationException("Assignment description cannot be empty");
        }
        if (maxScore == null || maxScore <= 0) {
            throw new ValidationException("maxScore must be a positive integer");
        }

        // -----------------------------
        // Загрузка урока
        // -----------------------------
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lesson not found: id=" + lessonId));

        // -----------------------------
        // Нормализация полей
        // -----------------------------
        String normalizedTitle = title.trim();
        String normalizedDescription = description.trim();

        // -----------------------------
        // Создание задания
        // -----------------------------
        Assignment assignment = new Assignment();
        assignment.setLesson(lesson);
        assignment.setTitle(normalizedTitle);
        assignment.setDescription(normalizedDescription);
        assignment.setDueDate(dueDate);
        assignment.setMaxScore(maxScore);

        return assignmentRepository.save(assignment);
    }

    // ============================================================================
    //                                     READ
    // ============================================================================

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
