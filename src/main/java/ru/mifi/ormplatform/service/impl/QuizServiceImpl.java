package ru.mifi.ormplatform.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Course;
import ru.mifi.ormplatform.domain.entity.Module;
import ru.mifi.ormplatform.domain.entity.Quiz;
import ru.mifi.ormplatform.repository.CourseRepository;
import ru.mifi.ormplatform.repository.ModuleRepository;
import ru.mifi.ormplatform.repository.QuizRepository;
import ru.mifi.ormplatform.service.QuizService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса работы с квизами.
 * Содержит проверку принадлежности модуля курсу,
 * нормализацию данных и корректную обработку ошибок.
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;

    public QuizServiceImpl(QuizRepository quizRepository,
                           CourseRepository courseRepository,
                           ModuleRepository moduleRepository) {
        this.quizRepository = quizRepository;
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
    }

    // ============================================================================
    //                               CREATE
    // ============================================================================

    @Override
    public Quiz createQuiz(Long courseId,
                           Long moduleId,
                           String title,
                           Integer timeLimitMinutes) {

        if (title == null || title.isBlank()) {
            throw new ValidationException("Quiz title cannot be empty");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Course not found: id=" + courseId));

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Module not found: id=" + moduleId));

        // Проверяем, что модуль принадлежит курсу
        if (!module.getCourse().getId().equals(courseId)) {
            throw new ValidationException(
                    "Module id=" + moduleId + " does not belong to course id=" + courseId);
        }

        // Проверяем отсутствие уже существующего квиза
        if (quizRepository.findByModule_Id(moduleId).isPresent()) {
            throw new ValidationException(
                    "Quiz for module id=" + moduleId + " already exists");
        }

        Quiz quiz = new Quiz();
        quiz.setModule(module);
        quiz.setTitle(title.trim());
        quiz.setTimeLimit(timeLimitMinutes);

        return quizRepository.save(quiz);
    }

    // ============================================================================
    //                             READ METHODS
    // ============================================================================

    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findById(Long id) {
        return quizRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Quiz> findByModule(Long moduleId) {
        return quizRepository.findByModule_Id(moduleId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> findByCourse(Long courseId) {

        // Получаем модули
        List<Module> modules =
                moduleRepository.findAllByCourse_IdOrderByOrderIndexAsc(courseId);

        // Преобразуем в список квизов
        return modules.stream()
                .map(m -> quizRepository.findByModule_Id(m.getId()))
                .flatMap(Optional::stream)
                .toList();
    }

    // ============================================================================
    //                               UPDATE
    // ============================================================================

    @Override
    public Quiz updateQuiz(Long id,
                           String title,
                           Integer timeLimitMinutes) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Quiz not found: id=" + id));

        if (title != null) {
            String normalized = title.trim();
            if (normalized.isEmpty()) {
                throw new ValidationException("Quiz title cannot be empty");
            }
            quiz.setTitle(normalized);
        }

        if (timeLimitMinutes != null) {
            quiz.setTimeLimit(timeLimitMinutes);
        }

        return quizRepository.save(quiz);
    }

    // ============================================================================
    //                               DELETE
    // ============================================================================

    @Override
    public void deleteQuiz(Long id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Quiz not found: id=" + id));

        quizRepository.delete(quiz);
    }
}
